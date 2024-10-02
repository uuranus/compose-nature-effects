package com.uuranus.compose.nature_effects.wind

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
) {

    var touchPoint by remember { mutableStateOf(Offset.Zero) }

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val confetties by remember(size, touchPoint) {
        mutableStateOf(
            if (size == IntSize.Zero || touchPoint == Offset.Zero) {
                emptyList() // Return empty list when size is zero
            } else {
                List(80) {
                    Confetti(
                        shape = if (Random.nextInt(2) == 0) RoundedCornerShape(0f) else CircleShape,
                        offset = Animatable(
                            touchPoint, Offset.VectorConverter
                        ),
                        color = randomPastelColor(),
                        rotation = Animatable(Random.nextFloat() * 360f),
                        alpha = Animatable(1f)
                    )
                }
            }
        )

    }

    var isFallingStart by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(size, touchPoint) {
        confetties.forEachIndexed { index, confetti ->

            val direction =
                if (index < confetties.size / 2) 1f else -1f

            val maxPeakHeight = size.height
            val peakHeight =
                maxPeakHeight * 0.2f + Random.nextFloat() * maxPeakHeight
            val heightScalingFactor = peakHeight / maxPeakHeight

            val horizontalDistance =
                heightScalingFactor * Random.nextFloat() * size.width

            launch {

                val frameCount = 30

                val controlPoint = Offset(
                    (touchPoint.x + touchPoint.x + direction * horizontalDistance) / 2,
                    touchPoint.y - peakHeight
                )
                val startPoint = touchPoint

                val peakPoint = Offset(
                    touchPoint.x + direction * horizontalDistance,
                    touchPoint.y - peakHeight
                )

                var lastBezierPosition = Offset.Zero

                repeat(frameCount) { i ->
                    val t = i / frameCount.toFloat()
                    val easedT = EaseOut.transform(t)
                    val bezierPosition =
                        quadraticBezier(easedT, startPoint, controlPoint, peakPoint)
                    confetti.offset.snapTo(bezierPosition)
                    lastBezierPosition = bezierPosition

                    if (bezierPosition == peakPoint) return@repeat
                    delay(16L)
                }

                isFallingStart = true

                val remainingDistance = size.height - lastBezierPosition.y

                val fallingDuration = (remainingDistance / 10).toInt()

                val endPoint = Offset(
                    peakPoint.x + direction * horizontalDistance / 2f,
                    size.height.toFloat()
                )

                val control2Point = Offset(
                    (peakPoint.x + endPoint.x) / 2f,
                    peakPoint.y
                )

                repeat(fallingDuration) { i ->
                    val t = i / fallingDuration.toFloat()
                    val easedT = LinearOutSlowInEasing.transform(t)
                    val bezierPosition = quadraticBezier(easedT, peakPoint, control2Point, endPoint)
                    confetti.offset.snapTo(bezierPosition)
                    delay(16L)
                }
            }

            launch {
                confetti.rotation.animateTo(
                    targetValue = confetti.rotation.value + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }
    }

    LaunchedEffect(isFallingStart) {
        if (isFallingStart) {
            confetties.forEach { confetti ->
                launch {
                    confetti.alpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(3000, easing = LinearEasing),
                    )
                }
            }
        }
    }

    Box(modifier = modifier
        .onGloballyPositioned {
            size = it.size
        }
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                touchPoint = tapOffset
            }
        }) {
        confetties.forEach {
            it.Draw()
        }
    }

}

data class Confetti(
    val shape: Shape,
    var offset: Animatable<Offset, AnimationVector2D>,
    val color: Color,
    var rotation: Animatable<Float, AnimationVector1D>,
    var alpha: Animatable<Float, AnimationVector1D>,
) {

    @Composable
    fun Draw() {
        Box(
            modifier =
            Modifier
                .offset {
                    IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt())
                }
                .size(
                    width = 10.dp,
                    height = 20.dp
                )
                .graphicsLayer {
                    rotationX = rotation.value
                    rotationY = rotation.value
                    rotationZ = rotation.value
                }
                .background(
                    color = color.copy(alpha = alpha.value),
                    shape = shape
                ))
    }
}

fun randomPastelColor(): Color {
    val hue = Random.nextFloat() * 360f       // Random hue [0, 360]
    val saturation = 0.6f + Random.nextFloat() * 0.3f  // Saturation [0.6, 0.9]
    val lightness = 0.6f + Random.nextFloat() * 0.1f   // Lightness [0.6, 0.7]

    return Color.hsl(hue, saturation, lightness)
}

fun quadraticBezier(t: Float, p0: Offset, p1: Offset, p2: Offset): Offset {
    val oneMinusT = (1 - t)

    val x = (oneMinusT * oneMinusT) * p0.x +
            2 * oneMinusT * t * p1.x +
            (t * t) * p2.x

    val y = (oneMinusT * oneMinusT) * p0.y +
            2 * oneMinusT * t * p1.y +
            (t * t) * p2.y

    return Offset(x, y)
}

fun cubicBezier(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Offset {
    val oneMinusT = (1 - t)

    val x = (oneMinusT * oneMinusT * oneMinusT) * p0.x +
            3 * (oneMinusT * oneMinusT) * t * p1.x +
            3 * oneMinusT * (t * t) * p2.x +
            (t * t * t) * p3.x

    val y = (oneMinusT * oneMinusT * oneMinusT) * p0.y +
            3 * (oneMinusT * oneMinusT) * t * p1.y +
            3 * oneMinusT * (t * t) * p2.y +
            (t * t * t) * p3.y

    return Offset(x, y)
}