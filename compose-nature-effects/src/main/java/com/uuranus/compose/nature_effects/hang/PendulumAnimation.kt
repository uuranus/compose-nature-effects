package com.uuranus.compose.nature_effects.hang

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

class PendulumAnimation(
    private val initialAngle: Float = 20f,
    private val dampingFactor: Float = 0.6f,
    private val startFromInitialAngle: Boolean,
) {

    private var rotation by mutableFloatStateOf(if (startFromInitialAngle) initialAngle else 0f)

    private var angle by mutableFloatStateOf(initialAngle)

    @Composable
    fun Start(trigger: Boolean) {
        StartHanging(isHanging = trigger)
    }

    @Composable
    fun StartHanging(isHanging: Boolean) {
        LaunchedEffect(isHanging) {
            if (isHanging) {
                launch {
                    while (angle >= 1f) {
                        if (startFromInitialAngle && angle == initialAngle) {
                            animate(
                                initialValue = angle,
                                targetValue = 0f,
                                animationSpec = keyframes {
                                    durationMillis = 300
                                    angle at 75 with LinearEasing
                                    0f at 150 with LinearEasing
                                    -angle at 225 with LinearEasing
                                    0f at 300 with LinearEasing
                                },
                            ) { value, _ ->
                                rotation = value
                            }
                        } else {
                            animate(
                                initialValue = 0f,
                                targetValue = 0f,
                                animationSpec = keyframes {
                                    durationMillis = 300
                                    0f at 0 with LinearEasing
                                    angle at 75 with LinearEasing
                                    0f at 150 with LinearEasing
                                    -angle at 225 with LinearEasing
                                    0f at 300 with LinearEasing
                                },
                            ) { value, _ ->
                                rotation = value
                            }

                        }

                        angle *= dampingFactor
                    }
                    angle = initialAngle
                }
            } else {
                angle = initialAngle
            }
        }

    }

    fun getCurrentRotation() = rotation
}