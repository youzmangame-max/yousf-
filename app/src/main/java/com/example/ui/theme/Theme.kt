package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MusicPrimary,
    onPrimary = MusicOnPrimary,
    primaryContainer = MusicPrimaryVariant,
    onPrimaryContainer = MusicOnPrimary,
    secondary = MusicSecondary,
    onSecondary = MusicOnPrimary,
    secondaryContainer = MusicSurfaceVariant,
    onSecondaryContainer = MusicOnSurface,
    tertiary = MusicTertiary,
    background = MusicBackground,
    onBackground = MusicOnSurface,
    surface = MusicSurface,
    onSurface = MusicOnSurface,
    surfaceVariant = MusicSurfaceVariant,
    onSurfaceVariant = MusicOnSurfaceVariant,
    outline = MusicOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek dark music theme
    dynamicColor: Boolean = false, // Keep consistent modern branding
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
