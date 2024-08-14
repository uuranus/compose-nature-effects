package com.uuranus.compose.nature_effects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

val yellowBackground = Color(0xFF8C8373)
val blueBackground = Color(0xfF0D1B2A)

@Composable
fun WindBlownEffect(
    backgroundColor: Color,
    lightColor: Color,
) {

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
                val y = Random.nextInt(screenHeightPx.toInt()).toFloat()
                Light(
                    Offset(x, y),
                    Random.nextFloat() * (PI / 2).toFloat() + (PI / 4).toFloat()
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

            delay(100L)
        }
    }

    val yellow = Color(0xFFFdFF99)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        lights.forEach { light ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(lightColor, Color.Transparent),
                    center = light.offset,
                    radius = 8.dp.toPx()
                ),
                center = light.offset,
            )
            drawCircle(
                color = lightColor,
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

            delay(100L)
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


@Composable
fun FloatingUpEffect(

) {

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        configuration.screenWidthDp * this.density
    }

    val screenHeightPx = with(density) {
        configuration.screenHeightDp * this.density
    }

    val shapeCriteria = with(density) {
        25.dp.toPx()
    }

    var bubbleState by remember {
        mutableStateOf(
            BubbleState(
                List(15) { index ->

                    val x = Random.nextInt(screenWidthPx.toInt()).toFloat()
                    val y = Random.nextInt(screenHeightPx.toInt()).toFloat()

                    val radius = with(density) {
                        if (index < 5) {
                            Random.nextInt(25, 30).dp.toPx()
                        } else {
                            Random.nextInt(10, 25).dp.toPx()
                        }
                    }

                    val shape: Shape = if (radius < shapeCriteria || Random.nextInt(2) == 0) {
                        CircleShape
                    } else {
                        RingShape(6.dp)
                    }

                    Bubble(
                        shape = shape,
                        offset = Offset(x, y),
                        radius = radius,
                        width = radius * 3,
                        angle = Random.nextFloat() * (PI / 2) + PI + (PI * 1 / 4)
                    )
                }
            )
        )
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            awaitFrame()
            bubbleState = bubbleState.copy(
                bubbles = bubbleState.bubbles.map { bubble ->
                    bubble.update(
                        Size(
                            screenWidthPx,
                            screenHeightPx
                        )
                    )
                    bubble
                }
            )

            delay(100L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF007FDE),
                        Color(0xFF004CC8)
                    )
                )
            )
    ) {

        for (bubble in bubbleState.bubbles) {
            val offsetX = with(density) {
                bubble.offset.x.toDp()
            }

            val offsetY = with(density) {
                bubble.offset.y.toDp()
            }

            val radiusDp = with(density) {
                bubble.radius.toDp()
            }

            Box(
                modifier = Modifier
                    .size(radiusDp * 2)
                    .offset(
                        x = offsetX,
                        y = offsetY
                    )
                    .scale(
                        if (bubble.shape == CircleShape) {
                            bubble.scale
                        } else {
                            1f
                        }
                    )
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = bubble.shape
                    )
            )

        }
    }

}


data class BubbleState(
    val bubbles: List<Bubble>,
)

class Bubble(
    val shape: Shape,
    offset: Offset,
    val radius: Float,
    val width: Float,
    angle: Double,
) {

    var offset by mutableStateOf(offset)
    private var angle by mutableDoubleStateOf(angle)
    var scale by mutableFloatStateOf(Random.nextFloat() * 0.4f + 0.6f)

    private var range = (offset.x - width / 2)..(offset.x + width / 2)

    private val increment = radius / 10f
    private var scaleIncrement = -0.01f

    fun update(
        screenSize: Size,
    ) {
        val newX = offset.x + increment * cos(angle).toFloat()
        val newY = offset.y + increment * sin(angle).toFloat()

        offset = if (newX < 0 || newX > screenSize.width || newY < 0) {
            val x = Random.nextFloat() * screenSize.width
            val y = screenSize.height + radius
            range = (x - width / 2)..(x + width / 2)
            Offset(x, y)
        } else if (newX !in range) {
            angle = (3 * PI / 2 - angle) + (3 * PI / 2)
            Offset(
                x = newX + cos(angle).toFloat(),
                y = newY
            )
        } else {
            Offset(newX, newY)
        }

        scale += scaleIncrement

        if (scale <= 0.6f || scale >= 1f) {
            scaleIncrement = -scaleIncrement
        }

    }

}

