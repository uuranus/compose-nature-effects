package com.uuranus.compose.nature_effects.weather

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
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

    var size by remember {
        mutableStateOf(Size.Zero)
    }

    val circleSize by animateFloatAsState(
        targetValue = if (isDay) 0f else 1f,
        animationSpec = tween(
            1000,
            easing = EaseIn
        ),
        label = "circleSize"
    )

//    LaunchedEffect(isDay) {
//        differenceCircleOffset.animateTo(
//            targetValue = if (isDay) {
//
//            } else {
//                Offset(
//                    size.width /2 +
//                )
//            },
//            animationSpec = tween(
//                1000,
//                easing = LinearEasing
//            ),
//        )
//    }

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
        val centerX = size.width / 2
        val centerY = size.height / 2

        val bigCircleRadius = size.minDimension / 3f
        val smallCircleRadius = bigCircleRadius * 1.2f

        val bigCirclePath = Path().apply {
            this.addOval(
                oval = Rect(
                    center = Offset(
                        centerX, centerY
                    ),
                    radius = bigCircleRadius
                )
            )
        }

        val differenceCirclePath = Path().apply {
            this.addOval(
                oval = Rect(
                    center = Offset(
                        centerX + (bigCircleRadius) * cos(PI / 4).toFloat(),
                        centerY - (bigCircleRadius) * sin(PI / 4).toFloat()
                    ),
                    radius = circleSize * smallCircleRadius,
                )
            )
        }

        bigCirclePath.apply {
            op(
                path1 = this,
                path2 = differenceCirclePath,
                operation = PathOperation.Difference
            )
        }

        drawPath(
            path = bigCirclePath,
            color = Color.Blue
        )
    }
}

