package com.uuranus.compose.nature_effects.water

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class WaveAnimation(
    private val waveProgressDuration: Int,
) {

    private var previousWaterHeight by mutableStateOf(0.dp)

    private var currentWaterHeight by mutableStateOf(previousWaterHeight)
    private var currentWaveProgresses by mutableStateOf(listOf<Float>())

    @Composable
    fun Start(
        waterHeight: Dp,
        width: Float,
        numOfWaves: Int,
    ) {
        UpdateWaterHeight(waterHeight)
        UpdateWaveProgress(width, numOfWaves)
    }

    @Composable
    fun UpdateWaterHeight(waterHeight: Dp) {

        val animatedHeight by animateDpAsState(
            targetValue = waterHeight,
            animationSpec = tween(
                durationMillis = waveProgressDuration,
                easing = LinearEasing
            ),
            label = "waterHeight"
        )

        LaunchedEffect(animatedHeight) {
            previousWaterHeight = waterHeight
        }

        currentWaterHeight = animatedHeight
    }

    @Composable
    fun UpdateWaveProgress(
        width: Float,
        numOfWaves: Int,
    ) {
        val progressAnimate = rememberInfiniteTransition(label = "waveProgress")

        currentWaveProgresses = List(numOfWaves) { waveIndex ->
            progressAnimate.animateFloat(
                initialValue = -waveIndex.toFloat(),
                targetValue = width - waveIndex.toFloat(),
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = waveProgressDuration,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "waveProgress"
            ).value + (width / numOfWaves) * waveIndex
        }
    }

    fun getWaveProgress(waveOffset: Float): Float {
        val waveNum = -waveOffset.toInt()
        return currentWaveProgresses.getOrElse(waveNum) { waveOffset }
    }

    fun getWaterHeight(): Float {
        return currentWaterHeight.value
    }

}

