package com.example.data.repository

import com.example.data.local.AchievementEntity
import com.example.data.local.GameRecordEntity
import com.example.data.local.UserDao
import com.example.data.local.UserProfileEntity
import com.example.data.model.AvatarFrame
import com.example.data.model.GameMode
import com.example.data.model.GameThemeMode
import com.example.data.model.LootBoxReward
import com.example.data.model.PowerUpType
import kotlinx.coroutines.flow.Flow

data class DailyRewardResult(
    val coinsAwarded: Int,
    val streakDay: Int
)

class QuizRepository(private val userDao: UserDao) {

    val userProfile: Flow<UserProfileEntity?> = userDao.getProfile()
    val recentGames: Flow<List<GameRecordEntity>> = userDao.getRecentGames()
    val achievements: Flow<List<AchievementEntity>> = userDao.getAllAchievements()

    private val defaultAchievements = listOf(
        AchievementEntity(
            id = "first_spark",
            title = "First Spark",
            description = "Complete your very first QuizRush round",
            badgeIcon = "RocketLaunch"
        ),
        AchievementEntity(
            id = "streak_5",
            title = "Overdrive",
            description = "Reach a combo streak of 5x",
            badgeIcon = "ElectricBolt"
        ),
        AchievementEntity(
            id = "streak_10",
            title = "Hyper Combo",
            description = "Reach an incredible combo streak of 10x",
            badgeIcon = "LocalFireDepartment"
        ),
        AchievementEntity(
            id = "speed_demon",
            title = "Speed Demon",
            description = "Answer a question correctly in under 2.0 seconds",
            badgeIcon = "Speed"
        ),
        AchievementEntity(
            id = "flawless",
            title = "Perfectionist",
            description = "Score 10/10 in Classic Rush",
            badgeIcon = "WorkspacePremium"
        ),
        AchievementEntity(
            id = "iron_will",
            title = "Iron Mind",
            description = "Survive 8 or more questions in Sudden Death",
            badgeIcon = "Shield"
        ),
        AchievementEntity(
            id = "time_master",
            title = "Chrono Warden",
            description = "Score 10+ correct answers in Blitz Mode",
            badgeIcon = "HourglassTop"
        ),
        AchievementEntity(
            id = "power_hoarder",
            title = "Armory Packed",
            description = "Hold 5 or more of any power-up in inventory",
            badgeIcon = "ShoppingBag"
        )
    )

    suspend fun initializeIfEmpty() {
        val existing = userDao.getProfileSync()
        if (existing == null) {
            userDao.saveProfile(UserProfileEntity())
        }
        userDao.insertDefaultAchievements(defaultAchievements)
    }

    suspend fun processDailyLogin(): DailyRewardResult? {
        val current = userDao.getProfileSync() ?: UserProfileEntity()
        val todayEpochDay = System.currentTimeMillis() / (1000L * 60 * 60 * 24)
        val lastDay = current.lastLoginEpochDay

        if (lastDay == todayEpochDay) {
            return null
        }

        val (newStreak, reward) = when {
            lastDay == 0L -> {
                1 to 60
            }
            todayEpochDay == lastDay + 1 -> {
                val streak = current.dailyLoginStreak + 1
                streak to (50 + (streak.coerceAtMost(7) * 20))
            }
            else -> {
                1 to 50
            }
        }

        val updated = current.copy(
            coins = current.coins + reward,
            dailyLoginStreak = newStreak,
            lastLoginEpochDay = todayEpochDay
        )
        userDao.saveProfile(updated)
        return DailyRewardResult(coinsAwarded = reward, streakDay = newStreak)
    }

    suspend fun recordGameResult(
        gameMode: GameMode,
        categoryName: String,
        score: Int,
        correctCount: Int,
        totalQuestions: Int,
        maxStreak: Int,
        fastestTimeSeconds: Float,
        earnedCoins: Int,
        earnedXp: Int
    ): UserProfileEntity {
        var current = userDao.getProfileSync() ?: UserProfileEntity()

        val newXp = current.xp + earnedXp
        val newCoins = current.coins + earnedCoins
        val newGamesPlayed = current.gamesPlayed + 1
        val newTotalCorrect = current.totalCorrect + correctCount
        val newHighestStreak = maxOf(current.highestStreak, maxStreak)

        val newFastestMs = if (fastestTimeSeconds > 0) {
            val ms = (fastestTimeSeconds * 1000).toLong()
            if (current.fastestAnswerMs == 99999L) ms else minOf(current.fastestAnswerMs, ms)
        } else {
            current.fastestAnswerMs
        }

        val newClassicHigh = if (gameMode == GameMode.CLASSIC) maxOf(current.highScoreClassic, score) else current.highScoreClassic
        val newSuddenHigh = if (gameMode == GameMode.SUDDEN_DEATH) maxOf(current.highScoreSuddenDeath, score) else current.highScoreSuddenDeath
        val newBlitzHigh = if (gameMode == GameMode.BLITZ) maxOf(current.highScoreBlitz, score) else current.highScoreBlitz

        val updatedProfile = current.copy(
            xp = newXp,
            coins = newCoins,
            gamesPlayed = newGamesPlayed,
            totalCorrect = newTotalCorrect,
            highestStreak = newHighestStreak,
            highScoreClassic = newClassicHigh,
            highScoreSuddenDeath = newSuddenHigh,
            highScoreBlitz = newBlitzHigh,
            fastestAnswerMs = newFastestMs
        )

        userDao.saveProfile(updatedProfile)

        // Save game history record
        userDao.insertGame(
            GameRecordEntity(
                gameMode = gameMode.title,
                category = categoryName,
                score = score,
                correctAnswers = correctCount,
                totalQuestions = totalQuestions,
                maxStreak = maxStreak,
                fastestTimeSeconds = fastestTimeSeconds
            )
        )

        // Evaluate achievements
        userDao.unlockAchievement("first_spark")
        if (maxStreak >= 5) userDao.unlockAchievement("streak_5")
        if (maxStreak >= 10) userDao.unlockAchievement("streak_10")
        if (fastestTimeSeconds in 0.01f..2.0f) userDao.unlockAchievement("speed_demon")
        if (gameMode == GameMode.CLASSIC && totalQuestions >= 10 && correctCount == totalQuestions) {
            userDao.unlockAchievement("flawless")
        }
        if (gameMode == GameMode.SUDDEN_DEATH && correctCount >= 8) {
            userDao.unlockAchievement("iron_will")
        }
        if (gameMode == GameMode.BLITZ && correctCount >= 10) {
            userDao.unlockAchievement("time_master")
        }
        if (updatedProfile.powerup5050 >= 5 || updatedProfile.powerupFreeze >= 5 || updatedProfile.powerupShield >= 5) {
            userDao.unlockAchievement("power_hoarder")
        }

        return updatedProfile
    }

    suspend fun buyPowerUp(type: PowerUpType): Boolean {
        val profile = userDao.getProfileSync() ?: return false
        if (profile.coins < type.cost) return false

        val updated = when (type) {
            PowerUpType.FIFTY_FIFTY -> profile.copy(
                coins = profile.coins - type.cost,
                powerup5050 = profile.powerup5050 + 1
            )
            PowerUpType.TIME_FREEZE -> profile.copy(
                coins = profile.coins - type.cost,
                powerupFreeze = profile.powerupFreeze + 1
            )
            PowerUpType.SHIELD -> profile.copy(
                coins = profile.coins - type.cost,
                powerupShield = profile.powerupShield + 1
            )
        }
        userDao.saveProfile(updated)

        if (updated.powerup5050 >= 5 || updated.powerupFreeze >= 5 || updated.powerupShield >= 5) {
            userDao.unlockAchievement("power_hoarder")
        }
        return true
    }

    suspend fun buyPowerUpBundle(): Boolean {
        val current = userDao.getProfileSync() ?: return false
        val bundleCost = 100
        if (current.coins < bundleCost) return false

        val updated = current.copy(
            coins = current.coins - bundleCost,
            powerup5050 = current.powerup5050 + 1,
            powerupFreeze = current.powerupFreeze + 1,
            powerupShield = current.powerupShield + 1
        )
        userDao.saveProfile(updated)
        return true
    }

    suspend fun unlockTheme(theme: GameThemeMode): Boolean {
        val current = userDao.getProfileSync() ?: return false
        val unlockedList = current.unlockedThemes.split(",").map { it.trim().uppercase() }
        if (unlockedList.contains(theme.id.uppercase())) {
            userDao.saveProfile(current.copy(currentTheme = theme.id))
            return true
        }
        if (current.coins < theme.unlockPrice) return false

        val newCoins = current.coins - theme.unlockPrice
        val newUnlocked = current.unlockedThemes + ",${theme.id}"
        userDao.saveProfile(
            current.copy(
                coins = newCoins,
                unlockedThemes = newUnlocked,
                currentTheme = theme.id
            )
        )
        return true
    }

    suspend fun equipTheme(theme: GameThemeMode): Boolean {
        val current = userDao.getProfileSync() ?: return false
        val unlockedList = current.unlockedThemes.split(",").map { it.trim().uppercase() }
        if (!unlockedList.contains(theme.id.uppercase())) return false

        userDao.saveProfile(current.copy(currentTheme = theme.id))
        return true
    }

    suspend fun consumePowerUp(type: PowerUpType): Boolean {
        val profile = userDao.getProfileSync() ?: return false
        val canConsume = when (type) {
            PowerUpType.FIFTY_FIFTY -> profile.powerup5050 > 0
            PowerUpType.TIME_FREEZE -> profile.powerupFreeze > 0
            PowerUpType.SHIELD -> profile.powerupShield > 0
        }
        if (!canConsume) return false

        val updated = when (type) {
            PowerUpType.FIFTY_FIFTY -> profile.copy(powerup5050 = profile.powerup5050 - 1)
            PowerUpType.TIME_FREEZE -> profile.copy(powerupFreeze = profile.powerupFreeze - 1)
            PowerUpType.SHIELD -> profile.copy(powerupShield = profile.powerupShield - 1)
        }
        userDao.saveProfile(updated)
        return true
    }

    suspend fun updatePlayerName(newName: String) {
        val profile = userDao.getProfileSync() ?: UserProfileEntity()
        userDao.saveProfile(profile.copy(playerName = newName.trim()))
    }

    suspend fun addLootBoxes(count: Int = 1) {
        val profile = userDao.getProfileSync() ?: return
        userDao.saveProfile(profile.copy(lootBoxesAvailable = profile.lootBoxesAvailable + count))
    }

    suspend fun openLootBox(): LootBoxReward? {
        val profile = userDao.getProfileSync() ?: return null
        if (profile.lootBoxesAvailable <= 0) return null

        val randomCoins = (120..350).random()
        val randomXp = (80..220).random()
        val powerUpTypes = PowerUpType.entries
        val rewardedPowerUp = powerUpTypes.random()
        val powerUpCount = (1..2).random()

        val updated = when (rewardedPowerUp) {
            PowerUpType.FIFTY_FIFTY -> profile.copy(
                lootBoxesAvailable = profile.lootBoxesAvailable - 1,
                coins = profile.coins + randomCoins,
                xp = profile.xp + randomXp,
                powerup5050 = profile.powerup5050 + powerUpCount
            )
            PowerUpType.TIME_FREEZE -> profile.copy(
                lootBoxesAvailable = profile.lootBoxesAvailable - 1,
                coins = profile.coins + randomCoins,
                xp = profile.xp + randomXp,
                powerupFreeze = profile.powerupFreeze + powerUpCount
            )
            PowerUpType.SHIELD -> profile.copy(
                lootBoxesAvailable = profile.lootBoxesAvailable - 1,
                coins = profile.coins + randomCoins,
                xp = profile.xp + randomXp,
                powerupShield = profile.powerupShield + powerUpCount
            )
        }
        userDao.saveProfile(updated)
        return LootBoxReward(
            coins = randomCoins,
            xp = randomXp,
            powerUpGranted = rewardedPowerUp,
            powerUpCount = powerUpCount
        )
    }

    suspend fun equipAvatarFrame(frame: AvatarFrame): Boolean {
        val current = userDao.getProfileSync() ?: return false
        userDao.saveProfile(current.copy(avatarFrame = frame.id))
        return true
    }

    suspend fun unlockAvatarFrame(frame: AvatarFrame): Boolean {
        val current = userDao.getProfileSync() ?: return false
        if (current.coins < frame.unlockPrice) return false
        userDao.saveProfile(
            current.copy(
                coins = current.coins - frame.unlockPrice,
                avatarFrame = frame.id
            )
        )
        return true
    }
}
