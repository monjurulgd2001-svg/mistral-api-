package com.volunteernews24.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.volunteernews24.app.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    onSplashFinished: () -> Unit
) {
    // Total duration = 4 seconds
    LaunchedEffect(key1 = true) {
        delay(4200)
        onSplashFinished()
    }

    var startAnimation by remember { mutableStateOf(false) }
    var exitAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(100)
        startAnimation = true
        delay(3200)
        exitAnimation = true
    }

    // === ENTRY animations ===

    // Overall scale: starts tiny, zooms to normal with overshoot
    val imageScale by animateFloatAsState(
        targetValue = when {
            exitAnimation -> 1.15f
            startAnimation -> 1f
            else -> 0.4f
        },
        animationSpec = if (exitAnimation)
            tween(durationMillis = 600, easing = FastOutSlowInEasing)
        else
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
        label = "imageScale"
    )

    // Alpha fade in
    val imageAlpha by animateFloatAsState(
        targetValue = when {
            exitAnimation -> 0f
            startAnimation -> 1f
            else -> 0f
        },
        animationSpec = if (exitAnimation)
            tween(durationMillis = 500, easing = FastOutLinearInEasing)
        else
            tween(durationMillis = 700),
        label = "imageAlpha"
    )

    // Slide up from bottom on entry
    val imageOffsetY by animateFloatAsState(
        targetValue = when {
            exitAnimation -> -80f
            startAnimation -> 0f
            else -> 120f
        },
        animationSpec = if (exitAnimation)
            tween(durationMillis = 500, easing = FastOutLinearInEasing)
        else
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
        label = "imageOffsetY"
    )

    // Rotation on entry: slight tilt then settles
    val imageRotation by animateFloatAsState(
        targetValue = if (startAnimation && !exitAnimation) 0f else if (exitAnimation) -5f else -8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "imageRotation"
    )

    // Continuous floating effect
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val floatY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    // Continuous glow pulse
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    // Background shimmer
    val shimmerX by infiniteTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {


        // Main splash image with all animations combined
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "VolunteerNews24 Splash",
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .aspectRatio(1f) // Square aspect ratio for the app icon
                .align(Alignment.Center)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                    alpha = imageAlpha
                    translationY = imageOffsetY + (if (startAnimation && !exitAnimation) floatY else 0f)
                    rotationZ = imageRotation
                },
            contentScale = ContentScale.Fit
        )

        // Bottom loading bar
        val loadingProgress by animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 3500, easing = LinearEasing),
            label = "loadingBar"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            // Track
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(3.dp)
                    .align(Alignment.Center)
                    .background(
                        Color.White.copy(alpha = 0.15f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                    )
            )
            // Progress
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f * loadingProgress)
                    .height(3.dp)
                    .align(Alignment.CenterStart)
                    .padding(start = (0.2f * 1f).dp) // centering offset
                    .graphicsLayer { alpha = imageAlpha }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF2020), Color(0xFFFF8800))
                            ),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}
