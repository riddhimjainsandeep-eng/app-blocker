package com.riddh.strictblocker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Updated Soft Minimalist Dark Palette
val SoftBlack = Color(0xFF121212)      // Deep background
val SoftCharcoal = Color(0xFF1E1E1E)   // Surface cards
val MutedSageGreen = Color(0xFF8DA38B) // Primary Accent
val MutedGrayButton = Color(0xFF3A3A3A) // Secondary Accent
val LightText = Color(0xFFE0E0E0)
val MutedText = Color(0xFFA0A0A0)

private val DarkColorScheme = darkColorScheme(
    primary = MutedSageGreen,
    secondary = MutedSageGreen,
    tertiary = MutedGrayButton,
    background = SoftBlack,
    surface = SoftCharcoal,
    onPrimary = SoftBlack,
    onSecondary = SoftBlack,
    onTertiary = LightText,
    onBackground = LightText,
    onSurface = LightText,
)

@Composable
fun DarkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
