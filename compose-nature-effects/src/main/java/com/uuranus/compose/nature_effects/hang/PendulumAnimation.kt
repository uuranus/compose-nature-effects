package com.uuranus.compose.nature_effects.hang

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs

class PendulumAnimation(
    val initialAngleDegree: Float = 20f,
    private val dampingFactor: Float = 0.6f,
) {

    private var startAngleDegree by mutableFloatStateOf(initialAngleDegree)
    private var currentAngleDegree by mutableFloatStateOf(startAngleDegree)

    private var rotationDegree by mutableFloatStateOf(currentAngleDegree)

    @Composable
    fun Start(trigger: Boolean) {
        StartHanging(isHanging = trigger)
    }

    fun updateInitialAngleDegree(updateAngleDegree: Float) {
        this.startAngleDegree = updateAngleDegree

        this.currentAngleDegree = updateAngleDegree
        this.rotationDegree = updateAngleDegree
    }

    @Composable
    fun StartHanging(isHanging: Boolean) {
        LaunchedEffect(isHanging) {
            if (isHanging) {
                while (abs(currentAngleDegree) >= 1f) {
                    animate(
                        initialValue = currentAngleDegree,
                        targetValue = 0f,
                        animationSpec = keyframes {
                            durationMillis = 300
                            0f at 0 with LinearEasing
                            currentAngleDegree at 75 with LinearEasing
                            0f at 150 with LinearEasing
                            -currentAngleDegree at 225 with LinearEasing
                            0f at 300 with LinearEasing
                        },
                    ) { value, _ ->
                        rotationDegree = value
                    }
                    currentAngleDegree *= dampingFactor
                }

            } else {
                currentAngleDegree = startAngleDegree
            }
        }
    }

    fun getCurrentRotationDegree() = rotationDegree

}