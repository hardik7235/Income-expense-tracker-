package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FrostedColorScheme = darkColorScheme(
    primary = FrostedIndigoLight,
    onPrimary = Color.White,
    primaryContainer = FrostedIndigoPill,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = FrostedEmeraldLight,
    onSecondary = Color(0xFF064E3B),
    secondaryContainer = FrostedEmeraldGlassBg,
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = FrostedRoseLight,
    onTertiary = Color(0xFF881337),
    tertiaryContainer = FrostedRoseGlassBg,
    onTertiaryContainer = Color(0xFFFFE4E6),
    background = FrostedDarkBg,
    onBackground = TextWhite,
    surface = FrostedDarkSurface,
    onSurface = TextWhite,
    surfaceVariant = Color(0xFF1E2433),
    onSurfaceVariant = TextMuted,
    outline = GlassBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Frosted Glass aesthetic defaults to dark canvas
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FrostedColorScheme,
        typography = Typography,
        content = content
    )
}
