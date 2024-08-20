package com.uuranus.compose.nature_effects.water

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.room.parser.expansion.Position
import com.uuranus.compose.nature_effects.Animation
import com.uuranus.compose.nature_effects.MyCanvas
import com.uuranus.compose.nature_effects.Picture
import kotlin.math.abs
import kotlin.math.sin

val purple = Color(0xff4b2cef)
val blue = Color(0xFF4478e3)
val mint = Color(0xFF1EB3B8)
val image1Colors = listOf(
    mint,
    mint.copy(alpha = 0.6f),
    mint.copy(alpha = 0.3f),
    Color.White.copy(alpha = 0.4f),
    Color.White.copy(alpha = 0.2f)
)

@Composable
fun WaveEffect(
    modifier: Modifier = Modifier,
    waterHeight: Dp,
    waveLevel: Float,
    numberOfWaves: Int = 3,
    numOfInterval: Int = 10,
) {

    val density = LocalDensity.current

    val scope = rememberCoroutineScope()
    val animation = remember {
        WaveAnimation(
            scope = scope,
            density = density,
            waterHeight = waterHeight,
            waveLevel = with(density) {
                20.dp.toPx()
            },
            waterHeightDuration = with(density) {
                waterHeight.toPx() / 100
            }.toInt(),
            waveProgressDuration = 1000
        )
    }

    val currentWaterHeight: Float = with(density) {
        animation.updateWaterHeight(waterHeight).toPx()
    }

    var width by remember {
        mutableFloatStateOf(0f)
    }

    val waveOffsets = animation.updateWaveProgress(width = width, numOfWaves = numberOfWaves)

    val waves = List(numberOfWaves) { index ->
        WavePicture(
            color = image1Colors[index],
            animation = animation,
            offset = Offset(waveOffsets[index], 0f),
            waveLevel = with(density) {
                20.dp.toPx()
            },
            waterHeight = currentWaterHeight
        )
    }

    MyCanvas(
        modifier = modifier,
        backgroundColor = Color.White,

        ) { drawScope ->
        width = drawScope.size.width

        for (wave in waves) {
            wave.draw(drawScope)
        }
    }
}


//    var previousWaterHeight by remember { mutableStateOf(waterHeight) }
//
//    val durationMillis = height / 100
//
//    val heightDifference by remember {
//        derivedStateOf {
//            with(density) {
//                abs(previousWaterHeight.toPx() - waterHeight.toPx())
//            }
//        }
//    }
//
//    val animatedWaterHeight by animateDpAsState(
//        targetValue = waterHeight,
//        animationSpec = tween(
//            durationMillis = (durationMillis * heightDifference).toInt(),
//            easing = LinearEasing
//        ), label = ""
//    )

//    val waterHeightPx = with(density) {
//        animatedWaterHeight.toPx()
//    }

//    val waterLevel = with(density) {
//        20.dp.toPx()
//    }

//    val progressAnimate = rememberInfiniteTransition(label = "")
//
//    val progresses = List(numberOfWaves) { waveIndex ->
//        progressAnimate.animateFloat(
//            initialValue = -waveIndex.toFloat(),
//            targetValue = width - waveIndex.toFloat(),
//            animationSpec = infiniteRepeatable(
//                animation = tween(
//                    durationMillis = 3000,
//                    easing = LinearEasing
//                ),
//                repeatMode = RepeatMode.Restart
//            ),
//            label = ""
//        ).value + (width / numberOfWaves) * waveIndex
//    }
//
//
//    LaunchedEffect(animatedWaterHeight) {
//        previousWaterHeight = waterHeight
//    }

//    Canvas(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//
//        val paths = List(numberOfWaves) { wave ->
//            Path().apply {
//
//                moveTo(offsets[wave][0].value.x, offsets[wave][0].value.y)
//
//                for (i in 1 until offsets[wave].size - 1) {
//
//                    val x = offsets[wave][i].value.x
//                    val y = offsets[wave][i].value.y
//
//                    val prevX = offsets[wave][i - 1].value.x
//                    val prevY = offsets[wave][i - 1].value.y
//
//                    val nextX = offsets[wave][i + 1].value.x
//                    val nextY = offsets[wave][i + 1].value.y
//
//                    cubicTo(
//                        (prevX + x) / 2,
//                        (prevY + y) / 2,
//                        x,
//                        y,
//                        (x + nextX) / 2,
//                        (y + nextY) / 2
//                    )
//
//                }
//
//                lineTo(width, height)
//                lineTo(0f, height)
//            }
//        }
//
//        for ((index, path) in paths.withIndex()) {
//            drawPath(path = path, color = image1Colors[index])
//        }
//
//    }

