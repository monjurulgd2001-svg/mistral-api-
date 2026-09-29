package com.volunteernews24.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(
    onSplashFinished: () -> Unit
) {
    // 4 Second timer to finish splash screen
    LaunchedEffect(key1 = true) {
        delay(4000)
        onSplashFinished()
    }

    // Animation States
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

    // --- Volunteer Text Animations ---
    val volunteerOffsetX by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -180f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    val volunteerAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    val volunteerScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    // --- News Text Animations ---
    val newsOffsetX by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -160f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 350, easing = FastOutSlowInEasing)
    )
    val newsAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 350)
    )

    // --- Globe Animations ---
    val globeScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1100, delayMillis = 500, easing = FastOutSlowInEasing)
    )
    val globeRotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -60f,
        animationSpec = tween(durationMillis = 1100, delayMillis = 500, easing = FastOutSlowInEasing)
    )
    val globeAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1100, delayMillis = 500)
    )
    
    // Continuous Globe Floating
    val infiniteTransition = rememberInfiniteTransition()
    val globeFloatY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // --- 24 Number Animations ---
    val number24OffsetX by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 150f,
        animationSpec = tween(durationMillis = 1150, delayMillis = 1000, easing = FastOutSlowInEasing)
    )
    val number24OffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -60f,
        animationSpec = tween(durationMillis = 1150, delayMillis = 1000, easing = FastOutSlowInEasing)
    )
    val number24Alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1150, delayMillis = 1000)
    )
    val number24Rotation by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 20f,
        animationSpec = tween(durationMillis = 1150, delayMillis = 1000, easing = FastOutSlowInEasing)
    )
    val number24Scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.35f,
        animationSpec = tween(durationMillis = 1150, delayMillis = 1000, easing = FastOutSlowInEasing)
    )

    // --- Welcome Text Animations ---
    val welcomeOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 80f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 1700, easing = FastOutSlowInEasing)
    )
    val welcomeAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 1700)
    )

    // Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Ambient Blue Glow in Background
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 20.dp)
                .size(220.dp)
                .graphicsLayer {
                    alpha = 0.15f
                    shadowElevation = 20f
                    shape = androidx.compose.foundation.shape.CircleShape
                    clip = true
                }
                .background(Color(0xFF0066FF))
        )
        
        // Ground Glow
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 120.dp)
                .size(width = 200.dp, height = 20.dp)
                .graphicsLayer {
                    alpha = 0.15f
                    shape = androidx.compose.foundation.shape.CircleShape
                    clip = true
                }
                .background(Color(0xFF008CFF))
        )

        // 3D Wireframe Globe
        Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
                .graphicsLayer {
                    alpha = globeAlpha
                    scaleX = globeScale
                    scaleY = globeScale
                    rotationZ = globeRotation
                    translationY = globeFloatY
                }
        ) {
            val radius = size.width / 2f
            
            // Outer Circle
            drawCircle(
                color = Color(0xFF55D5FF),
                radius = radius,
                style = Stroke(width = 3.dp.toPx())
            )
            
            // Longitudes (Vertical ellipses)
            drawOval(
                color = Color(0xFF8CDDFF),
                topLeft = Offset(radius - (radius * 0.5f), 0f),
                size = Size(radius, size.height),
                style = Stroke(width = 1.dp.toPx()),
                alpha = 0.45f
            )
            drawOval(
                color = Color(0xFF8CDDFF),
                topLeft = Offset(radius - (radius * 0.75f), 0f),
                size = Size(radius * 1.5f, size.height),
                style = Stroke(width = 1.dp.toPx()),
                alpha = 0.22f
            )

            // Latitudes (Horizontal ellipses)
            drawOval(
                color = Color(0xFF8CDDFF),
                topLeft = Offset(0f, radius - (radius * 0.3f)),
                size = Size(size.width, radius * 0.6f),
                style = Stroke(width = 1.dp.toPx()),
                alpha = 0.4f
            )
            drawOval(
                color = Color(0xFF8CDDFF),
                topLeft = Offset(0f, radius - (radius * 0.6f)),
                size = Size(size.width, radius * 1.2f),
                style = Stroke(width = 1.dp.toPx()),
                alpha = 0.22f
            )
            
            // Orbit Red
            drawOval(
                color = Color(0xFFFF2020),
                topLeft = Offset(-size.width * 0.2f, size.height * 0.1f),
                size = Size(size.width * 1.4f, size.height * 0.6f),
                style = Stroke(width = 3.dp.toPx())
            )
            
            // Orbit Blue
            drawOval(
                color = Color(0xFF188AFF),
                topLeft = Offset(-size.width * 0.1f, size.height * 0.2f),
                size = Size(size.width * 1.2f, size.height * 0.5f),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // The Number 24 (Inside Globe)
        Text(
            text = "24",
            color = Color(0xFFFF6969), // Red
            fontSize = 78.sp,
            fontWeight = FontWeight.ExtraBold,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(color = Color.Black, offset = Offset(0f, 6f), blurRadius = 4f)
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-10).dp, y = 5.dp)
                .graphicsLayer {
                    translationX = number24OffsetX
                    translationY = number24OffsetY
                    alpha = number24Alpha
                    rotationZ = number24Rotation
                    scaleX = number24Scale
                    scaleY = number24Scale
                }
        )

        // "Volunteer" Text
        Text(
            text = "Volunteer",
            color = Color(0xFFFF1515), // Red
            fontSize = 58.sp,
            fontWeight = FontWeight.ExtraBold,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(color = Color(0xFF720000), offset = Offset(5f, 5f), blurRadius = 0f)
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = (-110).dp)
                .padding(start = 24.dp)
                .graphicsLayer {
                    translationX = volunteerOffsetX
                    alpha = volunteerAlpha
                    scaleX = volunteerScale
                    scaleY = volunteerScale
                }
        )

        // "News" Text
        Text(
            text = "News",
            color = Color.White, // White
            fontSize = 68.sp,
            fontWeight = FontWeight.ExtraBold,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(color = Color(0xFF555555), offset = Offset(5f, 5f), blurRadius = 0f)
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(y = (-40).dp)
                .padding(start = 24.dp)
                .graphicsLayer {
                    translationX = newsOffsetX
                    alpha = newsAlpha
                }
        )

        // "Welcome" Text
        Text(
            text = "Welcome",
            color = Color(0xFFFFD500), // Yellow/Gold
            fontSize = 58.sp,
            fontWeight = FontWeight.ExtraBold,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(color = Color(0xFF996200), offset = Offset(5f, 5f), blurRadius = 0f)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-100).dp)
                .graphicsLayer {
                    translationY = welcomeOffsetY
                    alpha = welcomeAlpha
                }
        )
    }
}
