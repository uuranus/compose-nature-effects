package com.uuranus.compose.nature_effects.sample

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.uuranus.compose.nature_effects.water.wave.WaveEffect
import kotlinx.coroutines.delay


@Composable
fun WaterSample(
    modifier: Modifier = Modifier,
) {

    var size by remember {
        mutableStateOf(
            IntSize.Zero
        )
    }

    val density = LocalDensity.current

    val heightDp by remember {
        derivedStateOf {
            (with(density) {
                size.height.toDp()
            })
        }
    }

    val startHeight by remember {
        mutableStateOf(with(density) {
            (size.height / 10).toDp()
        })
    }

    var targetHeight by remember {
        mutableStateOf(
            startHeight
        )
    }

    LaunchedEffect(Unit) {
        while (targetHeight <= heightDp) {
            targetHeight += 30.dp
            delay(50)
        }
    }

    WaveEffect(
        modifier = modifier
            .width(500.dp)
            .aspectRatio(1f)
            .onGloballyPositioned {
                size = it.size
            },
        waveAmplitude = with(
            LocalDensity.current
        ) {
            20.dp.toPx()
        },
        waterHeight = targetHeight,
        waveProgressDuration = 2000,
        numberOfPeaks = 1
    )


}