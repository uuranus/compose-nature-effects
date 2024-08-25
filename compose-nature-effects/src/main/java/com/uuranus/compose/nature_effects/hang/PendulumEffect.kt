package com.uuranus.compose.nature_effects.hang

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PendulumEffect(
    modifier: Modifier = Modifier,
    initialAngle: Float = 20f,
    dampingFactor: Float = 0.6f,
    isHanging: Boolean,
    startFromInitialAngle: Boolean,
) {

    val animation = remember {
        PendulumAnimation(
            initialAngle = initialAngle,
            dampingFactor = dampingFactor,
            startFromInitialAngle = startFromInitialAngle
        )
    }

    var size by remember { mutableStateOf(IntSize.Zero) }

    animation.Start(trigger = isHanging)

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            },
        contentAlignment = Alignment.Center
    ) {
        val iconSize = with(LocalDensity.current) {
            minOf(size.width, size.height).toDp()
        }
        val radius = with(LocalDensity.current) {
            iconSize.toPx() / 2
        }

        Box(
            modifier = Modifier
                .size(iconSize)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer {

                        val centerY = size.height / 2
                        val angleRadians = Math.toRadians(
                            animation
                                .getCurrentRotation()
                                .toDouble()
                        )

                        this.translationX = -radius * sin(angleRadians).toFloat()
                        this.translationY = centerY - radius * cos(angleRadians).toFloat()

                        this.rotationZ = animation.getCurrentRotation()
                    }
            )
        }
    }
}