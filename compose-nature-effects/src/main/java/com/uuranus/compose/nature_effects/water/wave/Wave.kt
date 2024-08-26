package com.uuranus.compose.nature_effects.water.wave

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.uuranus.compose.nature_effects.Picture
import kotlin.math.sin

class WavePicture(
    val color: Color = Color.Blue,
    val animation: WaveAnimation,
    private val offset: Offset = Offset.Zero,
    private val waveAmplitude: Float = 0f,
    private val numberOfPeaks: Int,
) : Picture() {

    override fun draw(drawScope: DrawScope) {
        val size = drawScope.size

        val interval = (size.width / (numberOfPeaks * 2 + 1)).toInt()

        val offsets = List(interval) { index ->
            val xPosition = index * interval.toFloat()

            val progressX = animation.getWaveProgress(offset.x)

            val currentWaterHeight = animation.getWaterHeight()

            val relativeX = (xPosition - progressX) / size.width * 2 * Math.PI

            val yValue = sin(relativeX + Math.PI / 2) * waveAmplitude

            mutableStateOf(
                Offset(
                    interval * index.toFloat(),
                    size.height - currentWaterHeight + yValue.toFloat()
                )
            )
        }

        drawScope.drawPath(
            path = Path().apply {

                moveTo(offsets[0].value.x, offsets[0].value.y)

                for (i in 1 until offsets.size - 1) {

                    val x = offsets[i].value.x
                    val y = offsets[i].value.y

                    val prevX = offsets[i - 1].value.x
                    val prevY = offsets[i - 1].value.y

                    val nextX = offsets[i + 1].value.x
                    val nextY = offsets[i + 1].value.y

                    cubicTo(
                        (prevX + x) / 2,
                        (prevY + y) / 2,
                        x,
                        y,
                        (x + nextX) / 2,
                        (y + nextY) / 2
                    )
                }

                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            },
            color = color
        )
    }
}

