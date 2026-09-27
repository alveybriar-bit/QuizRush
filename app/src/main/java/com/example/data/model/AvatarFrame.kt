package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberYellow

enum class AvatarFrame(
    val id: String,
    val title: String,
    val borderColor: Color,
    val glowColor: Color,
    val unlockPrice: Int
) {
    NEON_CYAN(
        id = "NEON_CYAN",
        title = "Neon Cyan",
        borderColor = CyberCyan,
        glowColor = CyberCyan.copy(alpha = 0.5f),
        unlockPrice = 0
    ),
    GLITCH_PINK(
        id = "GLITCH_PINK",
        title = "Glitch Pink",
        borderColor = CyberPink,
        glowColor = CyberPink.copy(alpha = 0.5f),
        unlockPrice = 200
    ),
    GOLD_CHAMPION(
        id = "GOLD_CHAMPION",
        title = "Gold Champion",
        borderColor = CyberYellow,
        glowColor = CyberYellow.copy(alpha = 0.6f),
        unlockPrice = 350
    ),
    QUANTUM_VOID(
        id = "QUANTUM_VOID",
        title = "Quantum Void",
        borderColor = CyberPurple,
        glowColor = CyberPurple.copy(alpha = 0.6f),
        unlockPrice = 500
    ),
    SOLAR_FLAME(
        id = "SOLAR_FLAME",
        title = "Solar Flame",
        borderColor = CyberOrange,
        glowColor = CyberOrange.copy(alpha = 0.7f),
        unlockPrice = 650
    );

    companion object {
        fun fromId(id: String): AvatarFrame {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: NEON_CYAN
        }
    }
}

data class LootBoxReward(
    val coins: Int,
    val xp: Int,
    val powerUpGranted: PowerUpType,
    val powerUpCount: Int
)
