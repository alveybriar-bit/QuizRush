package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PowerUpType
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberYellow
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

// Cyberpunk Glass Card with subtle border glow
@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberCyan.copy(alpha = 0.4f),
    glowColor: Color = CyberCyan.copy(alpha = 0.15f),
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceElevated.copy(alpha = 0.92f),
                        SurfaceCard.copy(alpha = 0.95f),
                        SurfaceDark.copy(alpha = 0.98f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.1f), borderColor.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(16.dp)
    ) {
        content()
    }
}

// Neon Arcade Button with energetic feedback
@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = CyberCyan,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    testTag: String = "neon_button"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        color = if (enabled) color.copy(alpha = 0.15f) else Color.DarkGray.copy(alpha = 0.2f),
        border = BorderStroke(
            1.5.dp,
            if (enabled) color.copy(alpha = pulseAlpha) else Color.Gray.copy(alpha = 0.3f)
        ),
        modifier = modifier
            .testTag(testTag)
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = if (enabled) color else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = if (enabled) TextPrimary else TextMuted,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

// Power-Up button with inventory badge
@Composable
fun PowerUpButton(
    type: PowerUpType,
    count: Int,
    enabled: Boolean,
    isActive: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = when (type) {
        PowerUpType.FIFTY_FIFTY -> Icons.Default.CallSplit
        PowerUpType.TIME_FREEZE -> Icons.Default.AcUnit
        PowerUpType.SHIELD -> Icons.Default.Shield
    }

    val activeColor = if (isActive) CyberGreen else type.color

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isActive) activeColor.copy(alpha = 0.25f)
                else SurfaceElevated.copy(alpha = 0.8f)
            )
            .border(
                width = if (isActive) 2.dp else 1.dp,
                color = if (enabled) activeColor.copy(alpha = if (isActive) 1f else 0.5f) else Color.DarkGray,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(enabled = enabled && count > 0 && !isActive) { onClick() }
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .defaultMinSize(minHeight = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = type.title,
                tint = if (enabled && count > 0) activeColor else TextMuted,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = when (type) {
                        PowerUpType.FIFTY_FIFTY -> "50/50"
                        PowerUpType.TIME_FREEZE -> if (isActive) "FROZEN!" else "FREEZE"
                        PowerUpType.SHIELD -> if (isActive) "ACTIVE" else "SHIELD"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = if (enabled && count > 0) TextPrimary else TextMuted
                )
                Text(
                    text = "x$count left",
                    fontSize = 10.sp,
                    color = if (count > 0) activeColor else CyberRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Particle explosion simulation for high dopamine on correct answers
data class Particle(
    val id: Int,
    var x: Float,
    var y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    var alpha: Float = 1f
)

@Composable
fun ParticleBurstOverlay(
    trigger: Long,
    modifier: Modifier = Modifier
) {
    if (trigger == 0L) return

    val particles = remember(trigger) {
        mutableStateListOf<Particle>().apply {
            val colors = listOf(CyberCyan, CyberPink, CyberYellow, CyberGreen, CyberPurple)
            for (i in 0 until 40) {
                val angle = Random.nextDouble(0.0, Math.PI * 2)
                val speed = Random.nextFloat() * 14f + 4f
                add(
                    Particle(
                        id = i,
                        x = 0f,
                        y = 0f,
                        vx = (cos(angle) * speed).toFloat(),
                        vy = (sin(angle) * speed).toFloat() - 4f,
                        color = colors.random(),
                        size = Random.nextFloat() * 10f + 6f,
                        alpha = 1f
                    )
                )
            }
        }
    }

    var progress by remember(trigger) { mutableFloatStateOf(0f) }

    LaunchedEffect(trigger) {
        val anim = Animatable(0f)
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(700, easing = FastOutSlowInEasing)
        ) {
            progress = value
        }
    }

    if (progress < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height * 0.45f

            particles.forEach { p ->
                val curX = cx + p.vx * progress * 25f
                val curY = cy + p.vy * progress * 25f + (progress * progress * 80f)
                val curAlpha = (1f - progress).coerceIn(0f, 1f)
                val curSize = p.size * (1f - progress * 0.5f)

                drawCircle(
                    color = p.color.copy(alpha = curAlpha),
                    radius = curSize,
                    center = Offset(curX, curY)
                )
            }
        }
    }
}

// Floating Score notification that pops up and ascends
data class ScorePopupData(
    val id: Long,
    val text: String,
    val color: Color
)

@Composable
fun FloatingScoreBanner(
    popups: List<ScorePopupData>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        popups.takeLast(2).forEach { item ->
            var alpha by remember(item.id) { mutableFloatStateOf(1f) }
            var offsetY by remember(item.id) { mutableFloatStateOf(0f) }

            LaunchedEffect(item.id) {
                val anim = Animatable(0f)
                anim.animateTo(1f, tween(800, easing = FastOutSlowInEasing)) {
                    alpha = 1f - value
                    offsetY = -value * 70f
                }
            }

            Text(
                text = item.text,
                color = item.color,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .offset { IntOffset(0, offsetY.roundToInt()) }
                    .alpha(alpha)
            )
        }
    }
}

// Flame / Glow animated container when player hits 3+ win streak
@Composable
fun StreakFlameContainer(
    streak: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (streak < 3) {
        Box(modifier = modifier) {
            content()
        }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "flame_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val flameBrush = Brush.linearGradient(
        colors = listOf(
            CyberYellow,
            CyberOrange,
            CyberPink,
            CyberRed,
            CyberYellow
        )
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = (2.5f * pulse).dp,
                brush = flameBrush,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        CyberOrange.copy(alpha = 0.15f * pulse),
                        Color.Transparent
                    )
                )
            )
            .padding(3.dp)
    ) {
        content()
    }
}

// Daily login streak reward popup modal
@Composable
fun DailyRewardDialog(
    rewardCoins: Int,
    streakDay: Int,
    onClaim: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onClaim) {
        CyberCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            borderColor = CyberYellow
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(CyberYellow.copy(alpha = 0.2f))
                        .border(2.dp, CyberYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔥",
                        fontSize = 30.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "DAILY COMBO STREAK!",
                    color = CyberYellow,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "DAY $streakDay CONSECUTIVE LINK",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Neural link maintained! Your daily arcade currency has been transmitted:",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "🪙 +$rewardCoins RUSH COINS",
                        color = CyberYellow,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeonButton(
                    text = "CLAIM & ENTER ARENA",
                    onClick = onClaim,
                    color = CyberYellow,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "claim_daily_button"
                )
            }
        }
    }
}

