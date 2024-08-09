package com.uuranus.compose.nature_effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.sin

val image1Colors = listOf(
    Color(0xFF98D6E8), // Light Blue
    Color(0xFFE28A98), // Light Pink
    Color(0xFFB190E3), // Light Purple
    Color(0xFFA3E7B6), // Light Green
    Color(0xFFCACBFD), // Light Purple
    Color(0xFF8BC8F9), // Light Blue
    Color(0xFFDBA4F5), // Light Purple
)

@Composable
fun WaveEffect(
    numberOfWaves: Int = 1, // Number of simultaneous waves
) = BoxWithConstraints {

    val density = LocalDensity.current

    val height = with(density) {
        maxHeight.toPx()
    }
    val width = with(density) {
        maxWidth.toPx()
    }

    val interval = (width / 4).toInt()

    val waterHeight = height / 2

    val waterLevel = with(density) {
        20.dp.toPx()
    }

    val duration = 3000

    val progressAnimate = rememberInfiniteTransition(label = "")

    val progresses = List(numberOfWaves) { waveIndex ->
        progressAnimate.animateFloat(
            initialValue = 0f,
            targetValue = width,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = ""
        ).value + (width / numberOfWaves) * waveIndex
    }

    val offsets =
        List(numberOfWaves) { wave ->
            List(interval) { index ->
                val xPosition = index * interval.toFloat()

                val relativeX = (xPosition - progresses[wave]) / width * 2 * Math.PI

                val yValue = sin(relativeX + Math.PI / 2) * waterLevel

                mutableStateOf(
                    Offset(
                        interval * index.toFloat(),

                        waterHeight + yValue.toFloat()

                    )
                )
            }

        }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val arcSize = 20f
        val paths = List(numberOfWaves) { wave ->
            Path().apply {

                moveTo(offsets[wave][0].value.x, offsets[wave][0].value.y)

                for (i in 1 until offsets[wave].size - 1) {

                    val x = offsets[wave][i].value.x
                    val y = offsets[wave][i].value.y

                    val prevX = offsets[wave][i - 1].value.x
                    val prevY = offsets[wave][i - 1].value.y

                    val nextX = offsets[wave][i + 1].value.x
                    val nextY = offsets[wave][i + 1].value.y

                    cubicTo(
                        (prevX + x) / 2,
                        (prevY + y) / 2,
                        x,
                        y,
                        (x + nextX) / 2,
                        (y + nextY) / 2
                    )

                }

                lineTo(width, height)
                lineTo(0f, height)
            }
        }

        for ((index, path) in paths.withIndex()) {
            drawPath(path = path, color = image1Colors[index])

        }

    }
}
