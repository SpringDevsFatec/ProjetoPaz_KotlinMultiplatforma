package com.projetopaz.frontend_paz

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WireframeColorScheme = lightColorScheme(
    primary = Color(0xFF1E88E5),
    error = Color(0xFFD32F2F),
    background = Color(0xFFF4F4F4),
    surface = Color.White,
    onPrimary = Color.White,
    onError = Color(0xFFD32F2F),
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WireframeColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

