package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AchievementEntity
import com.example.data.model.PlayerRank
import com.example.ui.components.CyberCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun ProfileScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val achievements by viewModel.achievements.collectAsStateWithLifecycle()

    val currentXp = profile?.xp ?: 0
    val rank = PlayerRank.fromXp(currentXp)
    val nextRank = PlayerRank.nextRank(rank)

    val xpProgress = if (nextRank != null) {
        val needed = nextRank.minXp - rank.minXp
        val currentProgress = currentXp - rank.minXp
        (currentProgress.toFloat() / needed.toFloat()).coerceIn(0f, 1f)
    } else {
        1.0f
    }

    BackHandler {
        viewModel.setScreen(AppScreen.HOME)
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
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.setScreen(AppScreen.HOME) },
                    modifier = Modifier.testTag("profile_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "PILOT DOSSIER",
                    color = CyberCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Player Rank Banner Card
            CyberCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = rank.tierColor.copy(alpha = 0.5f)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(rank.tierColor, CyberCyan))
                                )
                                .border(2.dp, rank.tierColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = "Rank Emblem",
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = profile?.playerName ?: "CyberRunner",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = rank.title.uppercase(),
                                color = rank.tierColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Perk: ${rank.perk}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // XP Progress
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "EXPERIENCE",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (nextRank != null) "$currentXp / ${nextRank.minXp} XP" else "$currentXp XP (MAX)",
                                color = rank.tierColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { xpProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = rank.tierColor,
                            trackColor = SurfaceElevated
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Career Stats Grid
            Text(
                text = "CAREER TELEMETRY",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "GAMES PLAYED",
                    value = "${profile?.gamesPlayed ?: 0}",
                    color = CyberCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "CORRECT ANSWERS",
                    value = "${profile?.totalCorrect ?: 0}",
                    color = CyberGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "MAX CAREER STREAK",
                    value = "${profile?.highestStreak ?: 0}x",
                    color = CyberYellow,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "FASTEST REFLEX",
                    value = if ((profile?.fastestAnswerMs ?: 99999L) < 99999L) {
                        "${String.format("%.2f", (profile?.fastestAnswerMs ?: 0L) / 1000f)}s"
                    } else "-",
                    color = CyberBlue,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // High Scores Hall
            Text(
                text = "HALL OF RECORDS",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            CyberCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    HighScoreRow(
                        modeTitle = "Classic Rush",
                        score = profile?.highScoreClassic ?: 0,
                        color = CyberCyan
                    )
                    HighScoreRow(
                        modeTitle = "Sudden Death",
                        score = profile?.highScoreSuddenDeath ?: 0,
                        color = CyberPink
                    )
                    HighScoreRow(
                        modeTitle = "Blitz 60s",
                        score = profile?.highScoreBlitz ?: 0,
                        color = CyberYellow
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Unlocked Badges & Achievements
            Text(
                text = "ACHIEVEMENT BADGES (${achievements.count { it.isUnlocked }}/${achievements.size})",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                achievements.forEach { badge ->
                    val isUnlocked = badge.isUnlocked
                    val badgeColor = if (isUnlocked) CyberYellow else TextMuted

                    CyberCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = if (isUnlocked) CyberYellow.copy(alpha = 0.4f) else Color.DarkGray.copy(alpha = 0.3f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (isUnlocked) CyberYellow.copy(alpha = 0.2f) else SurfaceElevated)
                                    .border(1.dp, badgeColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isUnlocked) getBadgeVector(badge.badgeIcon) else Icons.Default.Lock,
                                    contentDescription = badge.title,
                                    tint = badgeColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = badge.title,
                                    color = if (isUnlocked) TextPrimary else TextMuted,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = badge.description,
                                    color = if (isUnlocked) TextSecondary else TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            if (isUnlocked) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CyberGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "UNLOCKED",
                                        color = CyberGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            NeonButton(
                text = "RETURN TO HUB",
                onClick = { viewModel.setScreen(AppScreen.HOME) },
                color = CyberCyan,
                modifier = Modifier.fillMaxWidth(),
                testTag = "profile_return_button"
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceElevated)
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun HighScoreRow(
    modeTitle: String,
    score: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = modeTitle,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
        Text(
            text = "$score PTS",
            color = color,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )
    }
}

fun getBadgeVector(iconName: String): ImageVector {
    return when (iconName) {
        "RocketLaunch" -> Icons.Default.RocketLaunch
        "ElectricBolt" -> Icons.Default.ElectricBolt
        "LocalFireDepartment" -> Icons.Default.LocalFireDepartment
        "Speed" -> Icons.Default.Speed
        "WorkspacePremium" -> Icons.Default.WorkspacePremium
        "Shield" -> Icons.Default.Shield
        "HourglassTop" -> Icons.Default.HourglassTop
        "ShoppingBag" -> Icons.Default.ShoppingBag
        else -> Icons.Default.EmojiEvents
    }
}
