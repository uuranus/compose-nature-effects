package com.uuranus.compose.nature_effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlin.math.PI
import kotlin.math.cos
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
fun WindBlownDiagonalEffect() {

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
            List(20) {
                val x = Random.nextInt(screenWidthPx.toInt()).toFloat()
                val y = Random.nextInt(screenHeightPx.toInt() / 2).toFloat()

                if (Random.nextInt(2) == 1) {
                    Offset(x, -y)
                } else {
                    Offset(screenWidthPx + x, y)
                }
            })
    }

    LaunchedEffect(Unit) {
        while (true) {
            things = things.map { offset ->
                val newX = offset.x - 10f // Adjust speed by changing the value
                val newY = offset.y + 10f // Adjust speed by changing the value

                if (newX <= 0 || newY >= screenHeightPx) {

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
            }
            // Add a delay for smooth animation
            kotlinx.coroutines.delay(100L) // Roughly 60fps
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        for (thing in things) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, Color.Transparent),
                    center = thing,
                    radius = 9.dp.toPx()
                ),
                center = thing,
            )
            drawCircle(
                color = Color.White,
                center = thing,
                radius = 5.dp.toPx()
            )
        }
    }


}