package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import com.example.ui.components.StreakFlameContainer
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Difficulty
import com.example.data.model.GameMode
import com.example.data.model.PowerUpType
import com.example.ui.components.CyberCard
import com.example.ui.components.FloatingScoreBanner
import com.example.ui.components.ParticleBurstOverlay
import com.example.ui.components.PowerUpButton
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel
import kotlin.math.roundToInt

@Composable
fun GamePlayScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.gameState.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("ABORT RUN?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("Progress and combo multipliers for this round will be abandoned.", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        viewModel.setScreen(AppScreen.HOME)
                    }
                ) {
                    Text("ABORT", color = CyberRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("RESUME", color = CyberCyan, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceElevated
        )
    }

    // Screen Shake effect calculation
    var shakeOffset by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(state.shakeTrigger) {
        if (state.shakeTrigger > 0L) {
            val anim = Animatable(0f)
            anim.animateTo(
                targetValue = 0f,
                animationSpec = tween(350, easing = LinearEasing)
            ) {
                // High frequency oscillation decaying to 0
                val progress = anim.value
                shakeOffset = kotlin.math.sin(progress * 30f) * (1f - progress) * 20f
            }
        }
    }

    val currentQ = state.currentQuestion

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .offset { IntOffset(shakeOffset.roundToInt(), 0) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Top HUD Bar: Back, Mode & Category, Score & Coins
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { showExitDialog = true },
                        modifier = Modifier.testTag("exit_game_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit run",
                            tint = TextPrimary
                        )
                    }

                    // Mode & Category pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = state.selectedMode.title.uppercase(),
                            color = state.selectedMode.accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = " · ${state.selectedCategory.displayName}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    // Score pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${state.score}",
                            color = CyberCyan,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "PTS",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Streak Multiplier & Shield Indicator Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Streak Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (state.currentStreak >= 3) CyberYellow.copy(alpha = 0.2f)
                                else SurfaceCard
                            )
                            .border(
                                width = 1.dp,
                                color = if (state.currentStreak >= 3) CyberYellow else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = if (state.currentStreak > 0) CyberYellow else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "STREAK ${state.currentStreak}",
                            color = if (state.currentStreak > 0) TextPrimary else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (state.streakMultiplier > 1.0f) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(${String.format("%.1f", state.streakMultiplier)}x)",
                                color = CyberYellow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // Shield indicator if active
                    if (state.isShieldActive) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberPink.copy(alpha = 0.2f))
                                .border(1.dp, CyberPink, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Shield Active",
                                tint = CyberPink,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SHIELD ARMED",
                                color = CyberPink,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Timer Bar & Countdown Text
                val isUrgent = state.timeRemainingSeconds <= 3.5f && !state.isTimerFrozen
                val timerColor = when {
                    state.isTimerFrozen -> CyberBlue
                    state.timeRemainingSeconds > 7f -> CyberGreen
                    state.timeRemainingSeconds > 3.5f -> CyberYellow
                    else -> CyberRed
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (state.isTimerFrozen) Icons.Default.AcUnit else Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = timerColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (state.isTimerFrozen) {
                                    "FROZEN (${String.format("%.1f", state.freezeSecondsLeft)}s)"
                                } else if (state.selectedMode == GameMode.BLITZ) {
                                    "TIME REMAINING: ${String.format("%.1f", state.timeRemainingSeconds)}s"
                                } else {
                                    "QUESTION TIMER: ${String.format("%.1f", state.timeRemainingSeconds)}s"
                                },
                                color = timerColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                        }

                        // Question Index (e.g. 3/10)
                        Text(
                            text = when (state.selectedMode) {
                                GameMode.CLASSIC -> "Q ${state.currentIndex + 1}/10"
                                GameMode.SUDDEN_DEATH -> "Q ${state.currentIndex + 1}"
                                GameMode.BLITZ -> "Q ${state.currentIndex + 1}"
                            },
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { state.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = timerColor,
                        trackColor = SurfaceElevated
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question Box & Interactive Options Area wrapped in StreakFlameContainer
                if (currentQ != null) {
                    StreakFlameContainer(
                        streak = state.currentStreak,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (state.currentStreak >= 3) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "🔥 OVERDRIVE ACTIVE · ${state.currentStreak}X STREAK (${String.format("%.1f", state.streakMultiplier)}x) 🔥",
                                        color = CyberYellow,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            CyberCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("question_card"),
                                borderColor = if (state.currentStreak >= 3) CyberYellow.copy(alpha = 0.7f) else state.selectedCategory.accentColor.copy(alpha = 0.45f)
                            ) {
                                Column {
                                    // Difficulty Tag
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    when (currentQ.difficulty) {
                                                        Difficulty.EASY -> CyberGreen.copy(alpha = 0.2f)
                                                        Difficulty.MEDIUM -> CyberYellow.copy(alpha = 0.2f)
                                                        Difficulty.HARD -> CyberPink.copy(alpha = 0.2f)
                                                    }
                                                )
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = currentQ.difficulty.name,
                                                color = when (currentQ.difficulty) {
                                                    Difficulty.EASY -> CyberGreen
                                                    Difficulty.MEDIUM -> CyberYellow
                                                    Difficulty.HARD -> CyberPink
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                        Text(
                                            text = currentQ.category.displayName,
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = currentQ.prompt,
                                        color = TextPrimary,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 23.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 4 Options Grid/Column
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                currentQ.options.forEachIndexed { index, optionText ->
                                    val isEliminated = state.eliminatedOptionIndices.contains(index)
                                    val isSelected = state.selectedOptionIndex == index
                                    val isCorrect = index == currentQ.correctIndex

                                    val optionBorderColor = when {
                                        state.isAnswerConfirmed && isCorrect -> CyberGreen
                                        state.isAnswerConfirmed && isSelected && !isCorrect -> CyberRed
                                        isSelected -> CyberCyan
                                        else -> Color.DarkGray.copy(alpha = 0.45f)
                                    }

                                    val optionBgColor = when {
                                        state.isAnswerConfirmed && isCorrect -> CyberGreen.copy(alpha = 0.32f)
                                        state.isAnswerConfirmed && isSelected && !isCorrect -> CyberRed.copy(alpha = 0.35f)
                                        isSelected -> CyberCyan.copy(alpha = 0.18f)
                                        isEliminated -> SurfaceDark.copy(alpha = 0.5f)
                                        else -> SurfaceElevated
                                    }

                                    val optionScale by androidx.compose.animation.core.animateFloatAsState(
                                        targetValue = if (state.isAnswerConfirmed && isCorrect) 1.03f else 1.0f,
                                        animationSpec = tween(250),
                                        label = "opt_scale"
                                    )

                                    Surface(
                                        onClick = {
                                            if (!isEliminated && !state.isAnswerConfirmed) {
                                                viewModel.submitAnswer(index)
                                            }
                                        },
                                        enabled = !isEliminated && !state.isAnswerConfirmed,
                                        shape = RoundedCornerShape(14.dp),
                                        color = optionBgColor,
                                        border = BorderStroke(
                                            if (state.isAnswerConfirmed && (isCorrect || isSelected)) 2.dp else 1.5.dp,
                                            optionBorderColor
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .scale(optionScale)
                                            .defaultMinSize(minHeight = 54.dp)
                                            .alpha(if (isEliminated) 0.35f else 1.0f)
                                            .testTag("option_$index")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Option Label Pill (A, B, C, D)
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (state.isAnswerConfirmed && isCorrect) CyberGreen
                                                        else if (state.isAnswerConfirmed && isSelected) CyberRed
                                                        else SurfaceCard
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (state.isAnswerConfirmed && isCorrect) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Correct",
                                                        tint = Color.Black,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                } else if (state.isAnswerConfirmed && isSelected && !isCorrect) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Wrong",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                } else {
                                                    Text(
                                                        text = ('A'.code + index).toChar().toString(),
                                                        color = if (isEliminated) TextMuted else TextPrimary,
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 13.sp
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Text(
                                                text = optionText,
                                                color = if (isEliminated) TextMuted else TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textDecoration = if (isEliminated) TextDecoration.LineThrough else null,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }

                            // Educational Explanation Banner with witty "DID YOU KNOW?" callout
                            AnimatedVisibility(
                                visible = state.isAnswerConfirmed,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 14.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(SurfaceCard)
                                        .border(1.5.dp, CyberCyan.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(CyberYellow.copy(alpha = 0.2f))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "⚡ DID YOU KNOW?",
                                                color = CyberYellow,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                    }

                                    if (currentQ.funFact.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = currentQ.funFact,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            lineHeight = 18.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = currentQ.explanation,
                                        color = TextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Power-Up Arsenal Bar
                Text(
                    text = "POWER-UP ARSENAL",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PowerUpButton(
                        type = PowerUpType.FIFTY_FIFTY,
                        count = profile?.powerup5050 ?: 0,
                        enabled = !state.isAnswerConfirmed && state.eliminatedOptionIndices.isEmpty(),
                        onClick = { viewModel.usePowerUp(PowerUpType.FIFTY_FIFTY) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("powerup_5050")
                    )

                    PowerUpButton(
                        type = PowerUpType.TIME_FREEZE,
                        count = profile?.powerupFreeze ?: 0,
                        enabled = !state.isAnswerConfirmed && !state.isTimerFrozen,
                        isActive = state.isTimerFrozen,
                        onClick = { viewModel.usePowerUp(PowerUpType.TIME_FREEZE) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("powerup_freeze")
                    )

                    PowerUpButton(
                        type = PowerUpType.SHIELD,
                        count = profile?.powerupShield ?: 0,
                        enabled = !state.isAnswerConfirmed && !state.isShieldActive,
                        isActive = state.isShieldActive,
                        onClick = { viewModel.usePowerUp(PowerUpType.SHIELD) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("powerup_shield")
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Confetti / Cyber Particles Overlay
            ParticleBurstOverlay(trigger = state.particleTrigger)

            // Floating Score Popups
            FloatingScoreBanner(popups = state.popups)
        }
    }
}
