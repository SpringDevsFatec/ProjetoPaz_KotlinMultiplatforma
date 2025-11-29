package com.projetopaz.frontend_paz.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cores do seu CSS
val PazBlack = Color(0xFF000000)
val PazWhite = Color(0xFFFFFFFF)
val PazGrayBgStart = Color(0xFFFFFFFF)
val PazGrayBgEnd = Color(0xFFF3F3F3)
val PazGrayText = Color(0xFF222222)
val PazCardBorder = Color(0xFFDDDDDD)

private val LightColorScheme = lightColorScheme(
    primary = PazBlack,
    onPrimary = PazWhite,
    background = PazWhite, // O gradiente faremos na tela
    surface = PazWhite,
    onSurface = PazGrayText,
    outline = PazCardBorder
)

val PazShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(10.dp), // Border-radius 10px do CSS
    large = RoundedCornerShape(16.dp)
)

val PazTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = PazGrayText
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = PazGrayText
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = Color(0xFF333333)
    )
)

@Composable
fun PazTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = PazTypography,
        shapes = PazShapes,
        content = content
    )
}