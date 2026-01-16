package com.productreview.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Splash Screen - Shows on app launch
 * Displays "Find Products You'll Love" with smooth animations
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    // Fade in animation
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    // Scale animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        // Start fade in
        startAnimation = true

        // Wait for animation to complete and display
        delay(2500)

        // Start fade out
        startAnimation = false

        // Wait for fade out
        delay(500)

        // Navigate to main screen
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Main text with "Love" in red
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Find Products You'll ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFE53935), // Red color for "Love"
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Love")
                    }
                },
                modifier = Modifier
                    .graphicsLayer(
                        alpha = alpha,
                        scaleX = scale,
                        scaleY = scale
                    )
            )

            // Stats row with animations
            Row(
                modifier = Modifier
                    .graphicsLayer(alpha = alpha),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatItem(
                    icon = "⭐",
                    value = "2.8",
                    label = "Avg Rating"
                )

                StatItem(
                    icon = "💬",
                    value = "121",
                    label = "Reviews"
                )

                StatItem(
                    icon = "📦",
                    value = "20",
                    label = "Products"
                )
            }
        }
    }
}

/**
 * Stat item component for splash screen
 */
@Composable
private fun StatItem(
    icon: String,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = icon,
            fontSize = 32.sp
        )
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
