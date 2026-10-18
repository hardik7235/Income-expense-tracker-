package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.FrostedDarkBg

@Composable
fun FrostedBackgroundWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FrostedDarkBg)
    ) {
        // Ambient Diffuse Glowing Spheres Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top-left Indigo glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x406366F1), // Indigo 25%
                        Color(0x204F46E5),
                        Color.Transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = size.width * 0.85f
                ),
                radius = size.width * 0.85f,
                center = Offset(0f, 0f)
            )

            // Bottom-right Teal/Emerald glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x3514B8A6), // Teal 20%
                        Color(0x1810B981),
                        Color.Transparent
                    ),
                    center = Offset(size.width, size.height),
                    radius = size.width * 0.8f
                ),
                radius = size.width * 0.8f,
                center = Offset(size.width, size.height)
            )

            // Mid subtle pink/violet orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x15DB2777),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.9f, size.height * 0.35f),
                    radius = size.width * 0.5f
                ),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.9f, size.height * 0.35f)
            )
        }

        content()
    }
}
