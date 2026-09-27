package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.GameThemeMode
import com.example.ui.components.DailyRewardDialog
import com.example.ui.components.LootBoxDialog
import com.example.ui.screens.GameOverScreen
import com.example.ui.screens.GamePlayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.StoreScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.QuizRushTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizRushApp()
        }
    }
}

@Composable
fun QuizRushApp(
    viewModel: QuizViewModel = viewModel()
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val dailyReward by viewModel.dailyRewardModal.collectAsStateWithLifecycle()
    val lootBoxReward by viewModel.lootBoxModal.collectAsStateWithLifecycle()
    val currentTheme = GameThemeMode.fromId(profile?.currentTheme ?: "CYBERPUNK")

    QuizRushTheme(themeMode = currentTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val state by viewModel.gameState.collectAsStateWithLifecycle()

            Crossfade(
                targetState = state.currentScreen,
                animationSpec = tween(300),
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.WELCOME -> WelcomeScreen(viewModel = viewModel)
                    AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                    AppScreen.PLAYING -> GamePlayScreen(viewModel = viewModel)
                    AppScreen.GAME_OVER -> GameOverScreen(viewModel = viewModel)
                    AppScreen.STORE -> StoreScreen(viewModel = viewModel)
                    AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                }
            }

            dailyReward?.let { reward ->
                DailyRewardDialog(
                    rewardCoins = reward.coinsAwarded,
                    streakDay = reward.streakDay,
                    onClaim = { viewModel.dismissDailyReward() }
                )
            }

            lootBoxReward?.let { reward ->
                LootBoxDialog(
                    reward = reward,
                    onClaim = { viewModel.dismissLootBoxModal() }
                )
            }
        }
    }
}
