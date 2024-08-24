package com.uuranus.compose.nature_effects.water.ripple

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class RippleAnimation(
    private val durationMillis: Int,
) {

    private var scale by mutableFloatStateOf(0f)
    private var rhombusScale by mutableFloatStateOf(0f)

    private var alpha by mutableFloatStateOf(1f)

    @Composable
    fun Start(trigger: Boolean) {
        UpdateRhombusScale(trigger)
        UpdateScale(trigger)
        UpdateAlpha(trigger)
    }

    @Composable
    private fun UpdateScale(trigger: Boolean) {
        LaunchedEffect(trigger) {
            animate(
                initialValue = 0f,
                targetValue = 1.2f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
            ) { value, _ ->
                scale = value
            }
        }
    }

    @Composable
    private fun UpdateAlpha(trigger: Boolean) {
        LaunchedEffect(trigger) {
            animate(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
            ) { value, _ ->
                alpha = value
            }
        }
    }

    @Composable
    private fun UpdateRhombusScale(trigger: Boolean) {

        val animatable = remember {
            Animatable(0f)
        }

        LaunchedEffect(trigger) {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = durationMillis * 6 / 10,
                    easing = LinearEasing
                ),
            ) {
                rhombusScale = value
            }

            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = durationMillis * 4 / 10,
                    easing = LinearEasing
                ),
            ) {
                rhombusScale = value
            }
        }
    }

    fun getCurrentScale(): Float = scale
    fun getCurrentAlpha(): Float = alpha
    fun getRhombusCurrentScale(): Float = rhombusScale
}