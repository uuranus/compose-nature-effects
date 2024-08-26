package com.uuranus.compose.nature_effects.hang

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import kotlin.math.atan2

@Composable
fun PendulumEffect(
    modifier: Modifier = Modifier,
    initialAngle: Float = 90f,
    dampingFactor: Float = 0.6f,
    startFromInitialAngle: Boolean,
) {
    var isHanging by remember {
        mutableStateOf(true)
    }

    var startPosition by remember {
        mutableStateOf(Offset.Zero)
    }

    var endPosition by remember {
        mutableStateOf(Offset.Zero)
    }

    val animation = remember {
        PendulumAnimation(
            initialAngleDegree = initialAngle,
            dampingFactor = dampingFactor,
            startFromInitialAngle = startFromInitialAngle
        )
    }

    var size by remember { mutableStateOf(IntSize.Zero) }

    animation.Start(trigger = isHanging)

    val pendulum = remember(size, animation) {
        Pendulum(
            size = size,
            animation = animation
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        isHanging = true
                    },
                    onDragStart = { position ->
                        isHanging = false

                        startPosition = position

                    },
                    onDrag = { change, _ ->
                        endPosition = change.position
                    }
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        val angle = calculateAngleDegree(startPosition, endPosition, size)
        animation.updateInitialAngleDegree(angle)

        pendulum.Draw(this)
    }
}

private fun calculateAngleDegree(startPosition: Offset, endPosition: Offset, size: IntSize): Float {
    val centerX = size.width / 2f
    val centerY = size.height / 2f

    val startDelta = atan2(
        startPosition.y - centerY,
        startPosition.x - centerX
    )
    val endDelta = atan2(
        endPosition.y - centerY,
        endPosition.x - centerX
    )

    var angleInRadians = endDelta - startDelta

    if (angleInRadians > Math.PI.toFloat()) {
        angleInRadians -= 2 * Math.PI.toFloat()
    } else if (angleInRadians < -Math.PI.toFloat()) {
        angleInRadians += 2 * Math.PI.toFloat()
    }

    var angleInDegrees = angleInRadians * (180 / Math.PI).toFloat()

    angleInDegrees = angleInDegrees.coerceIn(-90f, 90f)

    return angleInDegrees
}