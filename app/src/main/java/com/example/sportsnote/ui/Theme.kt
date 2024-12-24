package com.example.sportsnote.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SportsNoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

// Light Theme Colors
private val LightColorPalette = lightColors(
    primary = Color(0xFF3478F6),
    primaryVariant = Color(0xFF225AB7),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF018786),
    onSecondary = Color.Black
)

// Dark Theme Colors
private val DarkColorPalette = darkColors(
    primary = Color(0xFF3478F6),
    primaryVariant = Color(0xFF225AB7),
    onPrimary = Color.White,
    secondary = Color(0xFF03DAC5),
    secondaryVariant = Color(0xFF018786),
    onSecondary = Color.Black
)

private val Typography = Typography(
    h1 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

private val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp)
)