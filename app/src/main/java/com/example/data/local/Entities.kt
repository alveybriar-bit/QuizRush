package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "CyberRunner",
    val xp: Int = 0,
    val coins: Int = 150,
    val powerup5050: Int = 3,
    val powerupFreeze: Int = 2,
    val powerupShield: Int = 2,
    val gamesPlayed: Int = 0,
    val totalCorrect: Int = 0,
    val highestStreak: Int = 0,
    val highScoreClassic: Int = 0,
    val highScoreSuddenDeath: Int = 0,
    val highScoreBlitz: Int = 0,
    val fastestAnswerMs: Long = 99999L,
    val currentTheme: String = "CYBERPUNK",
    val unlockedThemes: String = "CYBERPUNK",
    val dailyLoginStreak: Int = 1,
    val lastLoginEpochDay: Long = 0L,
    val avatarFrame: String = "NEON_CYAN",
    val lootBoxesAvailable: Int = 1
)

@Entity(tableName = "game_records")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameMode: String,
    val category: String,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val maxStreak: Int,
    val fastestTimeSeconds: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val badgeIcon: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long = 0L
)
