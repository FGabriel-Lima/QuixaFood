package com.example.quixafood.ui.view.components


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PacManProgressBar(progress: Float) {
    val infiniteTransition = rememberInfiniteTransition()
    val mouthAngle by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        // Barra de fundo
        drawRoundRect(
            color = Color.LightGray,
            topLeft = Offset(x = 50f, y = size.height / 2 - 20),
            size = Size(width = size.width - 100, height = 40f),
            cornerRadius = CornerRadius(20f, 20f)
        )

        // Barra de progresso
        drawRoundRect(
            color = Color.Yellow,
            topLeft = Offset(x = 50f, y = size.height / 2 - 20),
            size = Size(width = (size.width - 100) * progress, height = 40f),
            cornerRadius = CornerRadius(20f, 20f)
        )

        // Desenhar o Pac-Man
        val pacManSize = 80f
        drawArc(
            color = Color.Yellow,
            startAngle = mouthAngle,
            sweepAngle = 360f - 2 * mouthAngle,
            useCenter = true,
            topLeft = Offset(
                x = 50f + (size.width - 100) * progress - pacManSize / 2,
                y = size.height / 2 - pacManSize / 2
            ),
            size = Size(pacManSize, pacManSize)
        )
    }
}
