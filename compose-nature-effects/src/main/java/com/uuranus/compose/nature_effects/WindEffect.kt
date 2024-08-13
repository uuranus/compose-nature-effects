package com.uuranus.compose.nature_effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

val yellowBackground = Color(0xFF8C8373)
val blueBackground = Color(0xfF0D1B2A)

@Composable
fun WindBlownEffect() {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        configuration.screenWidthDp * this.density
    }

    val screenHeightPx = with(density) {
        configuration.screenHeightDp * this.density
    }

    var lights by remember {
        mutableStateOf(
            List(20) {
                val x = Random.nextInt(screenWidthPx.toInt()).toFloat()
                val y = Random.nextInt(screenHeightPx.toInt() / 2).toFloat()
                Light(
                    Offset(x, -y),
                    Random.nextFloat() * (PI / 2).toFloat() + (PI / 4).toFloat() // Random angle for horizontal movement
                )
            }
        )
    }

    val amplitude = 6f

    LaunchedEffect(Unit) {
        while (true) {

            lights = lights.map { light ->
                val angle = light.angle

                val horizontalOffset = amplitude * cos(angle)
                val verticalOffset = amplitude * sin(angle)

                val newX = light.offset.x + horizontalOffset
                val newY = light.offset.y + verticalOffset

                if (newX <= 0 || newY >= screenHeightPx) {
                    val x = Random.nextInt(screenWidthPx.toInt()).toFloat()
                    val y = Random.nextInt(screenHeightPx.toInt() / 2).toFloat()

                    light.copy(offset = Offset(x, -y))
                } else {
                    light.copy(offset = Offset(newX, newY))
                }
            }
            kotlinx.coroutines.delay(100L)
        }
    }

    val yellow = Color(0xFFFdFF99)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(blueBackground)
    ) {

        lights.forEach { light ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(yellow, Color.Transparent),
                    center = light.offset,
                    radius = 8.dp.toPx()
                ),
                center = light.offset,
            )
            drawCircle(
                color = yellow,
                center = light.offset,
                radius = 5.dp.toPx()
            )
        }


    }
}

data class Light(
    val offset: Offset,
    val angle: Float,
)

@Composable
fun WindBlownDiagonalEffect(
    resourceId: Int,
) {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        configuration.screenWidthDp * this.density
    }

    val screenHeightPx = with(density) {
        configuration.screenHeightDp * this.density
    }

    var things by remember {
        mutableStateOf(
            List(10) {
                val x = Random.nextInt(screenWidthPx.toInt()).toFloat()
                val y = Random.nextInt(screenHeightPx.toInt() / 2).toFloat()

                val offset = if (Random.nextInt(2) == 1) {
                    Offset(x, -y)
                } else {
                    Offset(screenWidthPx + x, y)
                }

                val imageSize = Random.nextInt(10, 40).dp
                Leaf(offset, imageSize, 0f)
            })
    }

    LaunchedEffect(Unit) {
        while (true) {
            things = things.map { thing ->
                val newX = thing.offset.x - 10f
                val newY = thing.offset.y + 10f

                val offset = if (newX <= 0 || newY >= screenHeightPx) {

                    val x = Random.nextInt(screenWidthPx.toInt()).toFloat()
                    val y = Random.nextInt(screenHeightPx.toInt() / 2).toFloat()

                    if (Random.nextInt(2) == 1) {
                        Offset(x, -y)
                    } else {
                        Offset(screenWidthPx + x, y)
                    }
                } else {
                    Offset(newX, newY)
                }

                val rotate = (thing.rotate + 2) % 360
                thing.copy(offset = offset, rotate = rotate)
            }

            kotlinx.coroutines.delay(100L)
        }
    }

    val imageVector = ImageBitmap.imageResource(id = resourceId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        for (thing in things) {

            val offsetX = with(density) {
                thing.offset.x.toDp()
            }
            val offsetY = with(density) {
                thing.offset.y.toDp()
            }

            Image(
                bitmap = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(thing.size)
                    .offset(
                        x = offsetX,
                        y = offsetY
                    )
                    .rotate(thing.rotate),
                colorFilter = ColorFilter.tint(color = Color(0xFFAE8B4E))
            )
        }
    }
}

data class Leaf(
    val offset: Offset,
    val size: Dp,
    val rotate: Float,
)