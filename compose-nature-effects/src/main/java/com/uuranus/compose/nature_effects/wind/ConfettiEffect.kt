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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
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
fun ConfettiBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {

    val numOfConfetti = 80

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val confettiGroups = remember { mutableStateListOf<ConfettiGroup>() }

    var newStartIndex by remember {
        mutableIntStateOf(0)
    }

    fun cleanUpConfetti() {
        confettiGroups.removeIf { it.disappearedConfetti == it.confetti.size }
    }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(size, confettiGroups.size) {
        cleanUpConfetti()

        confettiGroups.forEach { group ->
            if (group.isAnimating) return@forEach
            group.isAnimating = true
            group.confetti.forEachIndexed { index, con ->
                val direction =
                    if (index < group.confetti.size / 2) 1f else -1f

                val maxPeakHeight = size.height
                val peakHeight =
                    maxPeakHeight * 0.2f + Random.nextFloat() * maxPeakHeight
                val heightScalingFactor = peakHeight / maxPeakHeight

                val horizontalDistance =
                    heightScalingFactor * Random.nextFloat() * size.width

                coroutineScope.launch {

                    val frameCount = 30

                    val controlPoint = Offset(
                        (group.touchPoint.x + group.touchPoint.x + direction * horizontalDistance) / 2,
                        group.touchPoint.y - peakHeight
                    )
                    val startPoint = group.touchPoint

                    val peakPoint = Offset(
                        group.touchPoint.x + direction * horizontalDistance,
                        group.touchPoint.y - peakHeight
                    )

                    var lastBezierPosition = Offset.Zero

                    repeat(frameCount) { i ->
                        val t = i / frameCount.toFloat()
                        val easedT = EaseOut.transform(t)
                        val bezierPosition =
                            quadraticBezier(easedT, startPoint, controlPoint, peakPoint)
                        con.offset.snapTo(bezierPosition)
                        lastBezierPosition = bezierPosition

                        if (bezierPosition == peakPoint) return@repeat
                        delay(16L)
                    }

                    coroutineScope.launch {
                        con.animateAlpha()
                    }

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
                        val bezierPosition =
                            quadraticBezier(easedT, peakPoint, control2Point, endPoint)
                        con.offset.snapTo(bezierPosition)
                        delay(16L)
                    }

                    group.disappearedConfetti++
                }

                coroutineScope.launch {
                    con.rotation.animateTo(
                        targetValue = con.rotation.value + 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        }

        newStartIndex = confettiGroups.size
    }

    Box(modifier = modifier
        .onGloballyPositioned {
            size = it.size
        }
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
                val newConfetti = List(numOfConfetti) {
                    Confetti(
                        shape = if (Random.nextInt(2) == 0) RoundedCornerShape(0f) else CircleShape,
                        offset = Animatable(tapOffset, Offset.VectorConverter),
                        color = randomPastelColor(),
                        rotation = Animatable(Random.nextFloat() * 360f),
                        alpha = Animatable(1f)
                    )
                }
                confettiGroups.add(
                    ConfettiGroup(newConfetti, tapOffset, size, false)
                )
            }
        }) {
        confettiGroups.forEach { group ->
            group.confetti.forEach {
                it.Draw()
            }
        }

        content()
    }
}

data class ConfettiGroup(
    val confetti: List<Confetti>,
    val touchPoint: Offset,
    val size: IntSize,
    var isAnimating: Boolean,
) {
    var disappearedConfetti = 0
}


data class Confetti(
    val shape: Shape,
    var offset: Animatable<Offset, AnimationVector2D>,
    val color: Color,
    var rotation: Animatable<Float, AnimationVector1D>,
    var alpha: Animatable<Float, AnimationVector1D>,
) {

    suspend fun animateAlpha() {
        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(3000, easing = LinearEasing),
        )
    }

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
                    height = 15.dp
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

    fun draw(drawScope: DrawScope) {
        drawScope.drawRect(
            color = color.copy(alpha = alpha.value),
            topLeft = offset.value,
            size = Size(10f, 20f),
        )
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

