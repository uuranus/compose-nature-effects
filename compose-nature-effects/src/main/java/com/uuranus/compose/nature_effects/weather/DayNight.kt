package com.uuranus.compose.nature_effects.weather

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DayNight(
    modifier: Modifier = Modifier,
    onChanged: (Boolean) -> Unit,
) {

    var isDay by remember {
        mutableStateOf(true)
    }

    var isAnimationDone by remember {
        mutableStateOf(true)
    }

    var size by remember {
        mutableStateOf(Size.Zero)
    }

    val animationDuration = 500

    val biteCircleSize by animateFloatAsState(
        targetValue = if (isDay) 0f else 1f,
        animationSpec = tween(
            animationDuration,
            easing = FastOutSlowInEasing,
        ),
        label = "biteCircleSize"
    ) {
        isAnimationDone = isDay
    }

    val moonCircleSize by animateFloatAsState(
        targetValue = if (isDay) 0.5f else 1f,
        animationSpec = tween(
            animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "circleSize"
    )

    val sunlight by animateFloatAsState(
        targetValue = if (isDay) 1f else 0.5f * 1.4f,
        animationSpec = tween(
            animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "sunlight"
    )

    val sunlightRotation by animateFloatAsState(
        targetValue = if (isDay) 360f else 0f,
        animationSpec = tween(
            animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "sunlightRotation"
    )

    val color = if (isSystemInDarkTheme()) Color.White else Color.Black

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    isDay = !isDay
                    onChanged(isDay)
                }
            }
            .onGloballyPositioned {
                size = it.size.toSize()
            }
    ) {

        val moonRadius = size.minDimension / 2f

        val biteCircleRadius = moonRadius * 2 / 3f
        val biteCircleOffset = Offset(
            center.x + moonRadius / 3,
            center.y - moonRadius / 3
        )

        val path = generateMoonPath(moonRadius * moonCircleSize)

        val differenceCirclePath = Path().apply {
            this.addOval(
                oval = Rect(
                    center = biteCircleOffset,
                    radius = biteCircleSize * biteCircleRadius,
                )
            )
        }

        path.apply {
            op(
                path1 = this,
                path2 = differenceCirclePath,
                operation = PathOperation.Difference
            )
        }

        if (isDay) {
            val sunRadius = moonRadius * moonCircleSize * 1.4f
            rotate(sunlightRotation) {
                drawPath(
                    path = generateSunlightPath(sunRadius, size.minDimension / 2f),
                    color = color,  // Sunlight Yellow
                    style = Stroke(
                        cap = StrokeCap.Round,
                        width = size.minDimension / 20f  // Slightly thinner sunlight
                    )
                )
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                cap = StrokeCap.Round,
                width = size.minDimension / 15f
            )
        )


    }
}

private fun DrawScope.generateSunlightPath(
    innerRadius: Float,
    outerSunlight: Float,
): Path {

    val numOfSunlight = 8

    val angle = 2 * PI / numOfSunlight

    var currentAngle = 0.0

    val path = Path()

    repeat(numOfSunlight) {
        val outPoint = Offset(
            center.x + (outerSunlight * cos(currentAngle)).toFloat(),
            center.y - (outerSunlight * sin(currentAngle)).toFloat()
        )
        val innerPoint = Offset(
            center.x + (innerRadius * cos(currentAngle)).toFloat(),
            center.y - (innerRadius * sin(currentAngle)).toFloat()
        )

        path.moveTo(
            outPoint.x,
            outPoint.y
        )

        path.lineTo(innerPoint.x, innerPoint.y)

        currentAngle += angle
    }

    return path
}


private fun DrawScope.generateMoonPath(moonRadius: Float): Path {


    return Path().apply {
        this.addOval(
            oval = Rect(
                center = Offset(
                    center.x, center.y
                ),
                radius = moonRadius
            )
        )
    }

}