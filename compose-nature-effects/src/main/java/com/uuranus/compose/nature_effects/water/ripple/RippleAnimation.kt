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
import kotlinx.coroutines.delay

class RippleAnimation(
    private val durationMillis: Int,
) {

    private var ringRadiusScale by mutableFloatStateOf(0f)
    private var rhombusRadiusScale by mutableFloatStateOf(0f)
    private var rhombusScale by mutableFloatStateOf(0f)

    private var ringAlpha by mutableFloatStateOf(1f)

    @Composable
    fun Start(trigger: Boolean) {
        UpdateRhombusRadius(trigger)
        UpdateRhombusScale(trigger)
        UpdateRingRadiusScale(trigger)
        UpdateRingAlpha(trigger)
    }

    @Composable
    private fun UpdateRingRadiusScale(trigger: Boolean) {
        LaunchedEffect(trigger) {
            animate(
                initialValue = 0f,
                targetValue = 1.2f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
            ) { value, _ ->
                ringRadiusScale = value
            }
        }
    }

    @Composable
    private fun UpdateRingAlpha(trigger: Boolean) {
        LaunchedEffect(trigger) {
            animate(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
            ) { value, _ ->
                ringAlpha = value
            }
        }
    }

    @Composable
    private fun UpdateRhombusRadius(trigger: Boolean) {
        LaunchedEffect(trigger) {
            animate(
                initialValue = 0.2f,
                targetValue = 1.3f,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
            ) { value, _ ->
                rhombusRadiusScale = value
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
                    durationMillis = durationMillis * 1 / 2,
                    easing = LinearEasing
                ),
            ) {
                rhombusScale = value
            }

            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = durationMillis * 1 / 2,
                    easing = LinearEasing
                ),
            ) {
                rhombusScale = value
            }
        }
    }

    fun getCurrentRingRadiusScale(): Float = ringRadiusScale
    fun getCurrentRingAlpha(): Float = ringAlpha
    fun getCurrentRhombusScale(): Float = rhombusScale
    fun getCurrentRhombusRadiusScale(): Float = rhombusRadiusScale
}