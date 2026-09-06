package com.example.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ISYCoral,
    onPrimary = ISYWhite,
    primaryContainer = ISYIndigo,
    onPrimaryContainer = ISYWhite,
    secondary = ISYIndigo,
    onSecondary = ISYWhite,
    background = ISYBlack,
    onBackground = ISYWhite,
    surface = ISYSurface,
    onSurface = ISYWhite,
    surfaceVariant = ISYSurfaceElevated,
    onSurfaceVariant = ISYGray300,
    outline = ISYGray500
)

@Composable
fun ISYouthTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
