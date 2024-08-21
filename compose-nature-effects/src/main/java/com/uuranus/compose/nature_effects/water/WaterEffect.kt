package com.uuranus.compose.nature_effects.water

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.uuranus.compose.nature_effects.MyCanvas

val purple = Color(0xff4b2cef)
val blue = Color(0xFF4478e3)
val mint = Color(0xFF1EB3B8)
val image1Colors = listOf(
    mint,
    mint.copy(alpha = 0.6f),
    mint.copy(alpha = 0.3f),
    Color.White.copy(alpha = 0.4f),
    Color.White.copy(alpha = 0.2f)
)

@Composable
fun WaveEffect(
    modifier: Modifier = Modifier,
    waterHeight: Dp,
    waveAmplitude: Float,
    numberOfWaves: Int = 3,
    numberOfPeaks: Int = 1,
    waveProgressDuration: Int,
) {

    var width by remember {
        mutableFloatStateOf(0f)
    }

    var height by remember {
        mutableFloatStateOf(0f)
    }

    val animation = remember {
        WaveAnimation(
            waveProgressDuration = waveProgressDuration
        )
    }

    animation.Start(waterHeight = waterHeight, width = width, numOfWaves = numberOfWaves)

    val waves = List(numberOfWaves) { index ->
        WavePicture(
            color = image1Colors[index],
            animation = animation,
            waveAmplitude = waveAmplitude,
            offset = Offset(-index.toFloat(), 0f),
            numberOfPeaks = numberOfPeaks
        )
    }

    MyCanvas(
        modifier = modifier,
        backgroundColor = Color.White
    ) { drawScope ->
        width = drawScope.size.width
        height = drawScope.size.height

        for (wave in waves) {
            wave.draw(drawScope)
        }
    }
}
