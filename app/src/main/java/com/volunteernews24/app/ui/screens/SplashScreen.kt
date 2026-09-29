package com.volunteernews24.app.ui.screens

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AnimatedSplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        // Wait for the HTML/CSS animation to finish (around 4.5 seconds based on CSS keyframes)
        delay(4500)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    // Ensure transparent background or black to match the HTML body
                    setBackgroundColor(android.graphics.Color.BLACK)
                    webViewClient = WebViewClient()
                    
                    // Load the highly complex 3D CSS/SVG animated code provided by the user
                    val encodedHtml = android.util.Base64.encodeToString(
                        SPLASH_HTML.toByteArray(Charsets.UTF_8), 
                        android.util.Base64.NO_PADDING
                    )
                    loadData(encodedHtml, "text/html", "base64")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
