package com.example.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Frosted Glass Core Colors
val FrostedDarkBg = Color(0xFF0A0A0A)
val FrostedDarkSurface = Color(0xFF12151D)
val GlassSurface = Color(0x0FFFFFFF) // White 6% opacity
val GlassSurfaceElevated = Color(0x17FFFFFF) // White 9% opacity
val GlassBorder = Color(0x24FFFFFF) // White 14% border
val GlassBorderSubtle = Color(0x14FFFFFF) // White 8% border

// Text & Accents
val TextWhite = Color(0xFFF8FAFC)
val TextMuted = Color(0xFF94A3B8)
val TextDarkMuted = Color(0xFF64748B)

// Vibrants
val FrostedIndigo = Color(0xFF6366F1)
val FrostedIndigoDark = Color(0xFF4F46E5)
val FrostedIndigoLight = Color(0xFF818CF8)
val FrostedIndigoPill = Color(0x336366F1)

val FrostedEmerald = Color(0xFF10B981)
val FrostedEmeraldLight = Color(0xFF34D399)
val FrostedEmeraldGlassBg = Color(0x1A10B981)
val FrostedEmeraldGlassBorder = Color(0x4010B981)

val FrostedRose = Color(0xFFF43F5E)
val FrostedRoseLight = Color(0xFFFB7185)
val FrostedRoseGlassBg = Color(0x1AF43F5E)
val FrostedRoseGlassBorder = Color(0x40F43F5E)

val FrostedTeal = Color(0xFF14B8A6)
val FrostedAmber = Color(0xFFF59E0B)

// Frosted Glass Mesh & Gradient Brushes
val FrostedGlassCardGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x22FFFFFF),
        Color(0x0CFFFFFF)
    )
)

val FrostedHeroBalanceGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x266366F1),
        Color(0x1A4F46E5),
        Color(0x150F172A),
        Color(0x20DB2777)
    )
)

val FrostedButtonGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF4F46E5),
        Color(0xFF6366F1)
    )
)
