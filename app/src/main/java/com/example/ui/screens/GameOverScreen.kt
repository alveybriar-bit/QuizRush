package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AvatarFrame
import com.example.data.model.GameMode
import com.example.data.model.PlayerRank
import com.example.ui.components.CyberCard
import com.example.ui.components.FlexCardDialog
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel
import kotlin.math.roundToInt

@Composable
fun GameOverScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.gameState.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler {
        viewModel.setScreen(AppScreen.HOME)
    }

    val totalQ = state.answerHistory.size
    val correctQ = state.answerHistory.count { it.isCorrect }
    val accuracy = if (totalQ > 0) ((correctQ.toFloat() / totalQ) * 100).roundToInt() else 0
    val avgResponseSeconds = if (totalQ > 0) {
        state.answerHistory.map { it.timeSpentSeconds }.average().toFloat()
    } else 0f

    val (rankGrade, performanceRating, ratingColor) = when {
        accuracy >= 90 && (avgResponseSeconds in 0.01f..4.0f) -> Triple("S+", "GODLIKE SYNAPSE OVERLOAD", CyberYellow)
        accuracy >= 80 -> Triple("S", "CYBER ELITE OPERATIVE", CyberCyan)
        accuracy >= 65 -> Triple("A", "SHARP TACTICAL RUNNER", CyberGreen)
        accuracy >= 50 -> Triple("B", "NEURAL APPRENTICE", CyberBlue)
        else -> Triple("C", "NEURAL REBOOT REQUIRED", CyberRed)
    }

    val isFlawless = totalQ >= 10 && correctQ == totalQ
    val isVictory = when (state.selectedMode) {
        GameMode.CLASSIC -> correctQ >= 7
        GameMode.SUDDEN_DEATH -> correctQ >= 5
        GameMode.BLITZ -> correctQ >= 8
    }

    var showQuestionReview by remember { mutableStateOf(false) }
    var showFlexCardModal by remember { mutableStateOf(false) }

    fun shareScoreCard() {
        val shareText = buildString {
            append("⚡ QUIZRUSH CYBER ARCADE ⚡\n")
            append("Performance: $rankGrade · $performanceRating\n")
            append("Mode: ${state.selectedMode.title} | ${state.selectedCategory.displayName}\n")
            append("Score: ${state.score} PTS\n")
            append("Accuracy: $accuracy% ($correctQ/$totalQ)\n")
            append("Avg Response: ${String.format("%.2f", avgResponseSeconds)}s\n")
            append("Max Streak: ${state.maxStreakAchieved}x 🔥\n")
            if (state.fastestAnswerSeconds < 50f) {
                append("Fastest Answer: ${String.format("%.2f", state.fastestAnswerSeconds)}s ⚡\n")
            }
            append("Coins Earned: +${state.sessionEarnedCoins} 🪙\n")
            append("Can you beat my QuizRush score? 🚀")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share QuizRush Scorecard")
        context.startActivity(shareIntent)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Victory / Game Over Header Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isVictory) CyberGreen.copy(alpha = 0.2f)
                        else CyberPink.copy(alpha = 0.2f)
                    )
                    .border(
                        1.dp,
                        if (isVictory) CyberGreen else CyberPink,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isFlawless) "⚡ FLAWLESS RUN ⚡"
                    else if (isVictory) "MISSION SUCCESSFUL"
                    else "RUN TERMINATED",
                    color = if (isVictory) CyberGreen else CyberPink,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Score Highlight
            Text(
                text = "${state.score}",
                color = CyberCyan,
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Text(
                text = "TOTAL SCORE",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Performance Rating Statement Card
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ratingColor.copy(alpha = 0.6f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Huge Grade Badge (S+, S, A, B, C)
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(ratingColor.copy(alpha = 0.2f))
                            .border(2.dp, ratingColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = rankGrade,
                            color = ratingColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PERFORMANCE RATING",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = performanceRating,
                            color = ratingColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Reflex: ${String.format("%.2f", avgResponseSeconds)}s avg · $accuracy% Accuracy",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // End-of-Game Analytics Dashboard (2x2 Grid + XP/Coins Bar)
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("analytics_dashboard")
            ) {
                Column {
                    Text(
                        text = "POST-MATCH TELEMETRY",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Accuracy
                        MetricTile(
                            label = "ACCURACY",
                            value = "$accuracy%",
                            subtext = "$correctQ / $totalQ Correct",
                            color = CyberGreen,
                            modifier = Modifier.weight(1f)
                        )

                        // Max Streak
                        MetricTile(
                            label = "MAX STREAK",
                            value = "${state.maxStreakAchieved}x",
                            subtext = "Combo Multiplier",
                            color = CyberYellow,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Average Response Time
                        MetricTile(
                            label = "AVG RESPONSE",
                            value = "${String.format("%.2f", avgResponseSeconds)}s",
                            subtext = "Per Question",
                            color = CyberBlue,
                            modifier = Modifier.weight(1f)
                        )

                        // Fastest Reflex
                        MetricTile(
                            label = "FASTEST REFLEX",
                            value = if (state.fastestAnswerSeconds < 50f) {
                                "${String.format("%.2f", state.fastestAnswerSeconds)}s"
                            } else "-",
                            subtext = "Speed Peak",
                            color = CyberPink,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Rewards Earned Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = "Coins Earned",
                                tint = CyberYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${state.sessionEarnedCoins} Coins",
                                color = CyberYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "XP Earned",
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${state.sessionEarnedXp} XP",
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Expandable Review Section
            CyberCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showQuestionReview = !showQuestionReview }
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Review",
                                tint = CyberYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "QUESTION REVIEW ($totalQ Qs)",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Icon(
                            imageVector = if (showQuestionReview) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Toggle Review",
                            tint = TextSecondary
                        )
                    }

                    AnimatedVisibility(visible = showQuestionReview) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            state.answerHistory.forEachIndexed { idx, res ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SurfaceElevated)
                                        .border(
                                            1.dp,
                                            if (res.isCorrect) CyberGreen.copy(alpha = 0.4f) else CyberRed.copy(alpha = 0.4f),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Q${idx + 1}: ${res.question.prompt}",
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = if (res.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = if (res.isCorrect) "Correct" else "Wrong",
                                            tint = if (res.isCorrect) CyberGreen else CyberRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "✓ Answer: ${res.question.options[res.question.correctIndex]}",
                                        color = CyberGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = res.question.explanation,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                    if (res.question.funFact.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "💡 FUN FACT: ${res.question.funFact}",
                                            color = CyberYellow,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons: Quick-Restart "PLAY AGAIN" & Share Score Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                NeonButton(
                    text = "PLAY AGAIN",
                    onClick = { viewModel.startNewGame(state.selectedMode, state.selectedCategory) },
                    leadingIcon = Icons.Default.Replay,
                    color = CyberCyan,
                    modifier = Modifier.weight(1f),
                    testTag = "play_again_button"
                )

                NeonButton(
                    text = "SHARE CARD",
                    onClick = { shareScoreCard() },
                    leadingIcon = Icons.Default.Share,
                    color = CyberPink,
                    modifier = Modifier.weight(1f),
                    testTag = "share_scorecard_button"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            NeonButton(
                text = "VIEW FLEX CARD",
                onClick = { showFlexCardModal = true },
                leadingIcon = Icons.Default.Visibility,
                color = CyberYellow,
                modifier = Modifier.fillMaxWidth(),
                testTag = "preview_flex_card_button"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                onClick = { viewModel.setScreen(AppScreen.HOME) },
                shape = RoundedCornerShape(12.dp),
                color = SurfaceElevated,
                border = BorderStroke(1.dp, Color.DarkGray.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .testTag("home_button")
            ) {
                Box(
                    modifier = Modifier.padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RETURN TO HUB",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        if (showFlexCardModal) {
            FlexCardDialog(
                playerName = profile?.playerName ?: "CyberRunner",
                avatarFrame = AvatarFrame.fromId(profile?.avatarFrame ?: "NEON_CYAN"),
                rank = PlayerRank.fromXp(profile?.xp ?: 0),
                score = state.score,
                accuracy = accuracy,
                avgResponseSeconds = avgResponseSeconds,
                maxStreak = state.maxStreakAchieved,
                grade = rankGrade,
                ratingStatement = performanceRating,
                gameMode = state.selectedMode,
                onDismiss = { showFlexCardModal = false }
            )
        }
    }
}

@Composable
private fun MetricTile(
    label: String,
    value: String,
    subtext: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = label,
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = color,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtext,
            color = TextSecondary,
            fontSize = 10.sp
        )
    }
}
