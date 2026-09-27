package com.example.data.model

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class GameThemeMode(
    val id: String,
    val title: String,
    val subtitle: String,
    val unlockPrice: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val backgroundColor: Color,
    val surfaceColor: Color,
    val cardColor: Color,
    val elevatedColor: Color
) {
    CYBERPUNK(
        id = "CYBERPUNK",
        title = "Cyberpunk Neon",
        subtitle = "Electric cyan, hot pink & dark cobalt grid",
        unlockPrice = 0,
        primaryColor = CyberCyan,
        secondaryColor = CyberPink,
        accentColor = CyberYellow,
        backgroundColor = BackgroundDark,
        surfaceColor = SurfaceDark,
        cardColor = SurfaceCard,
        elevatedColor = SurfaceElevated
    ),
    VAPORWAVE(
        id = "VAPORWAVE",
        title = "Vaporwave Retro",
        subtitle = "Synthwave magenta, aqua teal & retro violet",
        unlockPrice = 150,
        primaryColor = VaporPink,
        secondaryColor = VaporAqua,
        accentColor = VaporYellow,
        backgroundColor = VaporBackground,
        surfaceColor = VaporSurface,
        cardColor = VaporCard,
        elevatedColor = VaporElevated
    ),
    MIDNIGHT_OLED(
        id = "MIDNIGHT_OLED",
        title = "Midnight OLED",
        subtitle = "Zero nits true black, neon emerald & cyber amber",
        unlockPrice = 250,
        primaryColor = OledEmerald,
        secondaryColor = OledAmber,
        accentColor = OledBlue,
        backgroundColor = OledBackground,
        surfaceColor = OledSurface,
        cardColor = OledCard,
        elevatedColor = OledElevated
    );

    fun toColorScheme(): ColorScheme {
        return darkColorScheme(
            primary = primaryColor,
            onPrimary = backgroundColor,
            primaryContainer = elevatedColor,
            onPrimaryContainer = primaryColor,
            secondary = secondaryColor,
            onSecondary = Color.White,
            secondaryContainer = cardColor,
            onSecondaryContainer = secondaryColor,
            tertiary = accentColor,
            onTertiary = backgroundColor,
            background = backgroundColor,
            onBackground = TextPrimary,
            surface = surfaceColor,
            onSurface = TextPrimary,
            surfaceVariant = cardColor,
            onSurfaceVariant = TextSecondary,
            outline = primaryColor.copy(alpha = 0.35f),
            error = CyberRed,
            onError = Color.White
        )
    }

    companion object {
        fun fromId(id: String): GameThemeMode {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: CYBERPUNK
        }
    }
}
