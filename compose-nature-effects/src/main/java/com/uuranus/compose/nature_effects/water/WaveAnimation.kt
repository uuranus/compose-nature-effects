package com.uuranus.compose.nature_effects.water

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.uuranus.compose.nature_effects.Animation
import kotlinx.coroutines.CoroutineScope
import kotlin.math.abs

class WaveAnimation(
    private val scope: CoroutineScope,
    private val density: Density,
    private val waveLevel: Float,
    private var waterHeight: Dp,
    private val waterHeightDuration: Int,
    private val waveProgressDuration: Int,
) : Animation() {

    private var previousWaterHeight by mutableStateOf(waterHeight)

    private val heightDifference: Float
        get() = with(density) {
            abs(previousWaterHeight.toPx() - waterHeight.toPx())
        }

    @Composable
    fun updateWaterHeight(waterHeight: Dp): Dp {
        this.waterHeight = waterHeight

        val animatedHeight by animateDpAsState(
            targetValue = waterHeight,
            animationSpec = tween(
                durationMillis = (waterHeightDuration * heightDifference).toInt(),
                easing = LinearEasing
            ),
            label = ""
        )

        previousWaterHeight = waterHeight

        return animatedHeight
    }

    @Composable
    fun updateWaveProgress(
        width: Float,
        numOfWaves: Int,
    ): List<Float> {

        val progressAnimate = rememberInfiniteTransition(label = "")

        return List(numOfWaves) { waveIndex ->
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
                label = ""
            ).value + (width / numOfWaves) * waveIndex
        }
    }

    override fun getWaveOffsetY(): Float {
        TODO("Not yet implemented")
    }

    override fun getWaterHeight(): Float {
        return waterHeight.value
    }

}

