package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.data.model.GameThemeMode

@Composable
fun QuizRushTheme(
    themeMode: GameThemeMode = GameThemeMode.CYBERPUNK,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = themeMode.toColorScheme(),
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    QuizRushTheme(content = content)
}
