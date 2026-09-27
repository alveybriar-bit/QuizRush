package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberYellow

enum class Category(
    val id: String,
    val displayName: String,
    val iconName: String,
    val description: String,
    val accentColor: Color
) {
    ALL(
        id = "all",
        displayName = "Mega Mix",
        iconName = "Casino",
        description = "Random questions across all categories",
        accentColor = CyberCyan
    ),
    TECH(
        id = "tech",
        displayName = "Tech & Coding",
        iconName = "Code",
        description = "Algorithms, Frameworks & Silicon",
        accentColor = CyberCyan
    ),
    GAMING(
        id = "gaming",
        displayName = "Pop & Gaming",
        iconName = "SportsEsports",
        description = "Movies, Memes & AAA Blockbusters",
        accentColor = CyberPink
    ),
    SCIENCE(
        id = "science",
        displayName = "Science & Cosmos",
        iconName = "Science",
        description = "Physics, Space & Quantum Realm",
        accentColor = CyberGreen
    ),
    HISTORY(
        id = "history",
        displayName = "History & Lore",
        iconName = "AccountBalance",
        description = "Ancient Empires, Myths & Eras",
        accentColor = CyberYellow
    ),
    GEOGRAPHY(
        id = "geography",
        displayName = "Global & Geo",
        iconName = "Public",
        description = "Flags, Capitals, Borders & Wonders",
        accentColor = CyberBlue
    ),
    RIDDLES(
        id = "riddles",
        displayName = "Mind Benders",
        iconName = "Psychology",
        description = "Logic Puzzles & Lateral Thinking",
        accentColor = CyberPurple
    )
}

enum class GameMode(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val iconName: String,
    val accentColor: Color
) {
    CLASSIC(
        id = "classic",
        title = "Classic Rush",
        subtitle = "10 Questions Escalation",
        description = "Escalating timer (15s down to 5s), escalating score & streak multipliers.",
        iconName = "Timer",
        accentColor = CyberCyan
    ),
    SUDDEN_DEATH(
        id = "sudden_death",
        title = "Sudden Death",
        subtitle = "Endless · 1 Mistake = Out",
        description = "Endless questions. One wrong answer or timeout ends the run!",
        iconName = "Bolt",
        accentColor = CyberPink
    ),
    BLITZ(
        id = "blitz",
        title = "Blitz 60s",
        subtitle = "60-Second Time Attack",
        description = "Answer as many questions as possible in 60s. Correct answers add +2 bonus seconds!",
        iconName = "FlashOn",
        accentColor = CyberYellow
    )
}

enum class PowerUpType(
    val title: String,
    val description: String,
    val cost: Int,
    val iconName: String,
    val color: Color
) {
    FIFTY_FIFTY(
        title = "50/50 Glitch",
        description = "Eliminates 2 incorrect options",
        cost = 30,
        iconName = "CallSplit",
        color = CyberCyan
    ),
    TIME_FREEZE(
        title = "Cryo Freeze",
        description = "Freezes countdown for 5 seconds",
        cost = 45,
        iconName = "AcUnit",
        color = CyberBlue
    ),
    SHIELD(
        title = "Aegis Shield",
        description = "Protects streak & life from 1 wrong answer",
        cost = 60,
        iconName = "Shield",
        color = CyberPink
    )
}

enum class Difficulty {
    EASY, MEDIUM, HARD
}

data class Question(
    val id: String,
    val category: Category,
    val difficulty: Difficulty,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val funFact: String = ""
)

data class AnswerResult(
    val question: Question,
    val selectedIndex: Int,
    val isCorrect: Boolean,
    val timeSpentSeconds: Float,
    val pointsAwarded: Int,
    val usedShield: Boolean = false
)

enum class PlayerRank(
    val title: String,
    val minXp: Int,
    val tierColor: Color,
    val perk: String
) {
    NOVICE(
        title = "Novice Geek",
        minXp = 0,
        tierColor = CyberCyan,
        perk = "Standard Coin rate"
    ),
    SCHOLAR(
        title = "Cyber Scholar",
        minXp = 300,
        tierColor = CyberBlue,
        perk = "+10% Coin Bonus"
    ),
    PRODIGY(
        title = "Neural Prodigy",
        minXp = 800,
        tierColor = CyberPurple,
        perk = "Start runs with +1 Shield charge"
    ),
    VANGUARD(
        title = "Arcade Vanguard",
        minXp = 1600,
        tierColor = CyberYellow,
        perk = "+25% Coin Bonus"
    ),
    OVERLORD(
        title = "Vector Overlord",
        minXp = 2800,
        tierColor = CyberOrange,
        perk = "Reduced Power-Up Shop Costs"
    ),
    GRANDMASTER(
        title = "Quiz Grandmaster",
        minXp = 4500,
        tierColor = CyberPink,
        perk = "Double Streak Multiplier Cap"
    ),
    SINGULARITY(
        title = "Neon Singularity",
        minXp = 7000,
        tierColor = CyberGreen,
        perk = "Apex Trivia Deity: All Bonuses"
    );

    val accentColor: Color
        get() = tierColor

    companion object {
        fun fromXp(xp: Int): PlayerRank {
            val ranks = entries.sortedByDescending { it.minXp }
            return ranks.firstOrNull { xp >= it.minXp } ?: NOVICE
        }

        fun nextRank(current: PlayerRank): PlayerRank? {
            val index = entries.indexOf(current)
            return if (index < entries.size - 1) entries[index + 1] else null
        }
    }
}
