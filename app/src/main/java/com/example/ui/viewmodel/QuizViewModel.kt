package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AchievementEntity
import com.example.data.local.AppDatabase
import com.example.data.local.GameRecordEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.AnswerResult
import com.example.data.model.AvatarFrame
import com.example.data.model.Category
import com.example.data.model.GameMode
import com.example.data.model.GameThemeMode
import com.example.data.model.LootBoxReward
import com.example.data.model.PlayerRank
import com.example.data.model.PowerUpType
import com.example.data.model.Question
import com.example.data.repository.DailyRewardResult
import com.example.data.repository.QuestionBank
import com.example.data.repository.QuizRepository
import com.example.sound.GameFeedback
import com.example.ui.components.ScorePopupData
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberYellow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class AppScreen {
    WELCOME,
    HOME,
    PLAYING,
    GAME_OVER,
    STORE,
    PROFILE
}

data class QuizGameState(
    val currentScreen: AppScreen = AppScreen.WELCOME,
    val selectedMode: GameMode = GameMode.CLASSIC,
    val selectedCategory: Category = Category.ALL,
    val questions: List<Question> = emptyList(),
    val currentIndex: Int = 0,
    val timeRemainingSeconds: Float = 15f,
    val maxTimeForQuestion: Float = 15f,
    val isTimerFrozen: Boolean = false,
    val freezeSecondsLeft: Float = 0f,
    val isShieldActive: Boolean = false,
    val eliminatedOptionIndices: Set<Int> = emptySet(),
    val selectedOptionIndex: Int? = null,
    val isAnswerConfirmed: Boolean = false,
    val score: Int = 0,
    val currentStreak: Int = 0,
    val maxStreakAchieved: Int = 0,
    val streakMultiplier: Float = 1.0f,
    val sessionEarnedCoins: Int = 0,
    val sessionEarnedXp: Int = 0,
    val fastestAnswerSeconds: Float = 99f,
    val answerHistory: List<AnswerResult> = emptyList(),
    val particleTrigger: Long = 0L,
    val shakeTrigger: Long = 0L,
    val popups: List<ScorePopupData> = emptyList(),
    val questionStartTimeMs: Long = 0L
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentIndex)

    val progressPercent: Float
        get() = if (selectedMode == GameMode.BLITZ) {
            (timeRemainingSeconds / 60f).coerceIn(0f, 1f)
        } else {
            (timeRemainingSeconds / maxTimeForQuestion.coerceAtLeast(1f)).coerceIn(0f, 1f)
        }
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository
    val feedback: GameFeedback = GameFeedback(application)

    private val _gameState = MutableStateFlow(QuizGameState())
    val gameState: StateFlow<QuizGameState> = _gameState.asStateFlow()

    val userProfile: StateFlow<UserProfileEntity?>
    val recentGames: StateFlow<List<GameRecordEntity>>
    val achievements: StateFlow<List<AchievementEntity>>

    val dailyRewardModal: MutableStateFlow<DailyRewardResult?> = MutableStateFlow(null)
    val lootBoxModal: MutableStateFlow<LootBoxReward?> = MutableStateFlow(null)

    private var sessionCorrectStreakCounter: Int = 0
    private var timerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = QuizRepository(db.userDao())

        userProfile = repository.userProfile.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
        recentGames = repository.recentGames.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
        achievements = repository.achievements.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        viewModelScope.launch {
            repository.initializeIfEmpty()
            val reward = repository.processDailyLogin()
            if (reward != null) {
                delay(300)
                dailyRewardModal.value = reward
                feedback.onStreakBoost()
            }
        }
    }

    fun setScreen(screen: AppScreen) {
        _gameState.update { it.copy(currentScreen = screen) }
    }

    fun selectCategory(category: Category) {
        _gameState.update { it.copy(selectedCategory = category) }
    }

    fun selectMode(mode: GameMode) {
        _gameState.update { it.copy(selectedMode = mode) }
    }

    fun startNewGame(mode: GameMode = _gameState.value.selectedMode, category: Category = _gameState.value.selectedCategory) {
        timerJob?.cancel()

        val questionCount = when (mode) {
            GameMode.CLASSIC -> 10
            GameMode.SUDDEN_DEATH -> 30
            GameMode.BLITZ -> 40
        }

        val questions = QuestionBank.getQuestions(category, questionCount)
        if (questions.isEmpty()) return

        val initialTime = when (mode) {
            GameMode.CLASSIC -> 15f
            GameMode.SUDDEN_DEATH -> 12f
            GameMode.BLITZ -> 60f
        }

        val profile = userProfile.value
        val rank = PlayerRank.fromXp(profile?.xp ?: 0)
        // If high rank Neural Prodigy, give free shield
        val initialShield = rank.ordinal >= PlayerRank.PRODIGY.ordinal

        _gameState.value = QuizGameState(
            currentScreen = AppScreen.PLAYING,
            selectedMode = mode,
            selectedCategory = category,
            questions = questions,
            currentIndex = 0,
            timeRemainingSeconds = initialTime,
            maxTimeForQuestion = initialTime,
            isShieldActive = initialShield,
            questionStartTimeMs = System.currentTimeMillis()
        )

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var lastTickSecond = -1
            while (isActive) {
                delay(100)
                val state = _gameState.value
                if (state.currentScreen != AppScreen.PLAYING || state.isAnswerConfirmed) {
                    continue
                }

                // Handle Freeze
                if (state.isTimerFrozen) {
                    val newFreeze = state.freezeSecondsLeft - 0.1f
                    if (newFreeze <= 0f) {
                        _gameState.update { it.copy(isTimerFrozen = false, freezeSecondsLeft = 0f) }
                    } else {
                        _gameState.update { it.copy(freezeSecondsLeft = newFreeze) }
                    }
                    continue
                }

                val newTime = state.timeRemainingSeconds - 0.1f
                val wholeSec = newTime.toInt()

                if (wholeSec in 1..3 && wholeSec != lastTickSecond) {
                    lastTickSecond = wholeSec
                    feedback.onTimerTick()
                }

                if (newTime <= 0f) {
                    _gameState.update { it.copy(timeRemainingSeconds = 0f) }
                    handleTimeout()
                    break
                } else {
                    _gameState.update { it.copy(timeRemainingSeconds = newTime) }
                }
            }
        }
    }

    private fun handleTimeout() {
        val state = _gameState.value
        val curQ = state.currentQuestion ?: return
        feedback.onWrongAnswer()

        // Shield protection check
        if (state.isShieldActive) {
            feedback.onShieldAbsorbed()
            addPopup("SHIELD SAVED YOU!", CyberPink)
            _gameState.update {
                it.copy(
                    isShieldActive = false,
                    isAnswerConfirmed = true,
                    shakeTrigger = System.currentTimeMillis()
                )
            }
            proceedToNextQuestionDelayed(isCorrect = false, shielded = true)
            return
        }

        // Wrong/Timeout
        val newHistory = state.answerHistory + AnswerResult(
            question = curQ,
            selectedIndex = -1,
            isCorrect = false,
            timeSpentSeconds = state.maxTimeForQuestion,
            pointsAwarded = 0
        )

        _gameState.update {
            it.copy(
                isAnswerConfirmed = true,
                currentStreak = 0,
                streakMultiplier = 1.0f,
                answerHistory = newHistory,
                shakeTrigger = System.currentTimeMillis()
            )
        }

        if (state.selectedMode == GameMode.SUDDEN_DEATH) {
            finishGame()
        } else {
            proceedToNextQuestionDelayed(isCorrect = false)
        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val state = _gameState.value
        if (state.isAnswerConfirmed || state.currentScreen != AppScreen.PLAYING) return

        val curQ = state.currentQuestion ?: return
        val isCorrect = selectedIndex == curQ.correctIndex
        val elapsedSeconds = ((System.currentTimeMillis() - state.questionStartTimeMs) / 1000f).coerceAtLeast(0.1f)

        if (isCorrect) {
            feedback.onCorrectAnswer()

            val basePoints = when (curQ.difficulty) {
                com.example.data.model.Difficulty.EASY -> 100
                com.example.data.model.Difficulty.MEDIUM -> 150
                com.example.data.model.Difficulty.HARD -> 220
            }

            // Escalating speed bonus
            val speedBonus = if (elapsedSeconds < 3f) 50 else if (elapsedSeconds < 6f) 25 else 0
            val isClutch = state.timeRemainingSeconds <= 2.0f && !state.isTimerFrozen
            val clutchBonusXp = if (isClutch) 150 else 0
            val clutchBonusCoins = if (isClutch) 25 else 0

            val newStreak = state.currentStreak + 1
            val newMultiplier = (1.0f + (newStreak.coerceAtMost(6) * 0.25f))

            val earnedScore = ((basePoints + speedBonus) * newMultiplier).roundToInt()
            val earnedCoins = 10 + (newStreak * 2) + clutchBonusCoins
            val earnedXp = (earnedScore * 0.5f).roundToInt() + clutchBonusXp

            if (isClutch) {
                addPopup("🔥 CLUTCH +150 XP!", CyberYellow)
            }

            sessionCorrectStreakCounter++
            if (sessionCorrectStreakCounter % 5 == 0) {
                viewModelScope.launch {
                    repository.addLootBoxes(1)
                }
                addPopup("🎁 MYSTERY LOOT EARNED!", CyberYellow)
                feedback.onStreakBoost()
            }

            if (newStreak % 3 == 0) {
                feedback.onStreakBoost()
            }

            val newHistory = state.answerHistory + AnswerResult(
                question = curQ,
                selectedIndex = selectedIndex,
                isCorrect = true,
                timeSpentSeconds = elapsedSeconds,
                pointsAwarded = earnedScore
            )

            val fastest = if (state.fastestAnswerSeconds == 99f) elapsedSeconds else minOf(state.fastestAnswerSeconds, elapsedSeconds)

            // Blitz bonus time
            val updatedTime = if (state.selectedMode == GameMode.BLITZ) {
                (state.timeRemainingSeconds + 2.0f).coerceAtMost(75f)
            } else {
                state.timeRemainingSeconds
            }

            addPopup("+$earnedScore PTS!", CyberGreen)
            if (newStreak >= 3) {
                addPopup("STREAK x${String.format("%.1f", newMultiplier)}!", CyberYellow)
            }

            _gameState.update {
                it.copy(
                    selectedOptionIndex = selectedIndex,
                    isAnswerConfirmed = true,
                    score = it.score + earnedScore,
                    currentStreak = newStreak,
                    maxStreakAchieved = maxOf(it.maxStreakAchieved, newStreak),
                    streakMultiplier = newMultiplier,
                    sessionEarnedCoins = it.sessionEarnedCoins + earnedCoins,
                    sessionEarnedXp = it.sessionEarnedXp + earnedXp,
                    fastestAnswerSeconds = fastest,
                    answerHistory = newHistory,
                    timeRemainingSeconds = updatedTime,
                    particleTrigger = System.currentTimeMillis()
                )
            }

            proceedToNextQuestionDelayed(isCorrect = true)
        } else {
            // Wrong answer
            if (state.isShieldActive) {
                feedback.onShieldAbsorbed()
                addPopup("SHIELD SAVED YOU!", CyberPink)
                _gameState.update {
                    it.copy(
                        selectedOptionIndex = selectedIndex,
                        isAnswerConfirmed = true,
                        isShieldActive = false,
                        shakeTrigger = System.currentTimeMillis()
                    )
                }
                proceedToNextQuestionDelayed(isCorrect = false, shielded = true)
            } else {
                feedback.onWrongAnswer()
                val newHistory = state.answerHistory + AnswerResult(
                    question = curQ,
                    selectedIndex = selectedIndex,
                    isCorrect = false,
                    timeSpentSeconds = elapsedSeconds,
                    pointsAwarded = 0
                )

                _gameState.update {
                    it.copy(
                        selectedOptionIndex = selectedIndex,
                        isAnswerConfirmed = true,
                        currentStreak = 0,
                        streakMultiplier = 1.0f,
                        answerHistory = newHistory,
                        shakeTrigger = System.currentTimeMillis()
                    )
                }

                if (state.selectedMode == GameMode.SUDDEN_DEATH) {
                    finishGame()
                } else {
                    proceedToNextQuestionDelayed(isCorrect = false)
                }
            }
        }
    }

    private fun proceedToNextQuestionDelayed(isCorrect: Boolean, shielded: Boolean = false) {
        viewModelScope.launch {
            // Give user time to see correct/incorrect color feedback
            delay(1200)

            val state = _gameState.value
            val nextIndex = state.currentIndex + 1

            val isClassicComplete = state.selectedMode == GameMode.CLASSIC && nextIndex >= 10
            val isOutOfQuestions = nextIndex >= state.questions.size

            if (isClassicComplete || isOutOfQuestions) {
                finishGame()
            } else {
                // Next question with escalating timer for Classic mode
                val nextTime = when (state.selectedMode) {
                    GameMode.CLASSIC -> {
                        // Escalates from 15s down to 5s at question 10
                        (15f - (nextIndex * 1.1f)).coerceAtLeast(5f)
                    }
                    GameMode.SUDDEN_DEATH -> (12f - (nextIndex * 0.3f)).coerceAtLeast(6f)
                    GameMode.BLITZ -> state.timeRemainingSeconds // Continues counting down 60s
                }

                _gameState.update {
                    it.copy(
                        currentIndex = nextIndex,
                        selectedOptionIndex = null,
                        isAnswerConfirmed = false,
                        eliminatedOptionIndices = emptySet(),
                        isTimerFrozen = false,
                        timeRemainingSeconds = nextTime,
                        maxTimeForQuestion = if (state.selectedMode == GameMode.BLITZ) 60f else nextTime,
                        questionStartTimeMs = System.currentTimeMillis()
                    )
                }
            }
        }
    }

    private fun finishGame() {
        timerJob?.cancel()
        feedback.onGameOver()

        val state = _gameState.value
        val correctCount = state.answerHistory.count { it.isCorrect }

        viewModelScope.launch {
            repository.recordGameResult(
                gameMode = state.selectedMode,
                categoryName = state.selectedCategory.displayName,
                score = state.score,
                correctCount = correctCount,
                totalQuestions = state.answerHistory.size,
                maxStreak = state.maxStreakAchieved,
                fastestTimeSeconds = if (state.fastestAnswerSeconds == 99f) 0f else state.fastestAnswerSeconds,
                earnedCoins = state.sessionEarnedCoins,
                earnedXp = state.sessionEarnedXp
            )
        }

        _gameState.update { it.copy(currentScreen = AppScreen.GAME_OVER) }
    }

    fun usePowerUp(type: PowerUpType) {
        val state = _gameState.value
        val curQ = state.currentQuestion ?: return
        if (state.isAnswerConfirmed || state.currentScreen != AppScreen.PLAYING) return

        viewModelScope.launch {
            val consumed = repository.consumePowerUp(type)
            if (!consumed) return@launch

            feedback.onPowerUpUsed()

            when (type) {
                PowerUpType.FIFTY_FIFTY -> {
                    if (state.eliminatedOptionIndices.isNotEmpty()) return@launch
                    val wrongIndices = curQ.options.indices.filter { it != curQ.correctIndex }.shuffled().take(2)
                    _gameState.update {
                        it.copy(eliminatedOptionIndices = wrongIndices.toSet())
                    }
                    addPopup("50/50 GLITCH!", CyberCyan)
                }
                PowerUpType.TIME_FREEZE -> {
                    _gameState.update {
                        it.copy(isTimerFrozen = true, freezeSecondsLeft = 5.0f)
                    }
                    addPopup("FREEZE (5s)!", CyberCyan)
                }
                PowerUpType.SHIELD -> {
                    _gameState.update {
                        it.copy(isShieldActive = true)
                    }
                    addPopup("SHIELD READY!", CyberPink)
                }
            }
        }
    }

    fun dismissDailyReward() {
        dailyRewardModal.value = null
    }

    fun unlockTheme(theme: GameThemeMode) {
        viewModelScope.launch {
            val success = repository.unlockTheme(theme)
            if (success) {
                feedback.onStreakBoost()
                addPopup("${theme.title.uppercase()} UNLOCKED!", theme.primaryColor)
            } else {
                feedback.onWrongAnswer()
                addPopup("NOT ENOUGH COINS!", CyberRed)
            }
        }
    }

    fun equipTheme(theme: GameThemeMode) {
        viewModelScope.launch {
            val success = repository.equipTheme(theme)
            if (success) {
                feedback.onPowerUpUsed()
                addPopup("${theme.title.uppercase()} ACTIVE!", theme.primaryColor)
            }
        }
    }

    fun buyPowerUpBundle() {
        viewModelScope.launch {
            val success = repository.buyPowerUpBundle()
            if (success) {
                feedback.onStreakBoost()
                addPopup("TRI-PACK ARSENAL READY!", CyberYellow)
            } else {
                feedback.onWrongAnswer()
                addPopup("NOT ENOUGH COINS!", CyberRed)
            }
        }
    }

    fun buyPowerUp(type: PowerUpType) {
        viewModelScope.launch {
            val success = repository.buyPowerUp(type)
            if (success) {
                feedback.onPowerUpUsed()
                addPopup("+1 ${type.title}!", type.color)
            } else {
                feedback.onWrongAnswer()
                addPopup("NOT ENOUGH COINS!", CyberRed)
            }
        }
    }

    fun onEnterRush() {
        feedback.onPowerUpUsed()
        setScreen(AppScreen.HOME)
    }

    fun openLootBox() {
        viewModelScope.launch {
            val reward = repository.openLootBox()
            if (reward != null) {
                feedback.onStreakBoost()
                lootBoxModal.value = reward
                addPopup("LOOT UNLOCKED! +${reward.coins} COINS", CyberYellow)
            } else {
                feedback.onWrongAnswer()
                addPopup("NO LOOT BOXES READY!", CyberRed)
            }
        }
    }

    fun dismissLootBoxModal() {
        lootBoxModal.value = null
    }

    fun updatePlayerName(newName: String) {
        viewModelScope.launch {
            repository.updatePlayerName(newName)
            addPopup("PILOT UPDATED!", CyberCyan)
        }
    }

    fun equipAvatarFrame(frame: AvatarFrame) {
        viewModelScope.launch {
            val success = repository.equipAvatarFrame(frame)
            if (success) {
                feedback.onPowerUpUsed()
                addPopup("${frame.title.uppercase()} FRAME READY!", frame.borderColor)
            }
        }
    }

    fun unlockAvatarFrame(frame: AvatarFrame) {
        viewModelScope.launch {
            val success = repository.unlockAvatarFrame(frame)
            if (success) {
                feedback.onStreakBoost()
                addPopup("${frame.title.uppercase()} UNLOCKED!", frame.borderColor)
            } else {
                feedback.onWrongAnswer()
                addPopup("NOT ENOUGH COINS!", CyberRed)
            }
        }
    }

    private fun addPopup(text: String, color: androidx.compose.ui.graphics.Color) {
        val item = ScorePopupData(System.currentTimeMillis() + (0..100).random(), text, color)
        _gameState.update { it.copy(popups = (it.popups + item).takeLast(3)) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        feedback.release()
    }
}
