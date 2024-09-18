package com.uuranus.compose.nature_effects.water.ripple

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.uuranus.compose.nature_effects.MyCanvas

@Composable
fun RippleEffect(
    modifier: Modifier = Modifier,
    radius: Float = 200f,
) {
    var touchPos by remember { mutableStateOf(Offset.Zero) }
    var trigger by remember { mutableStateOf(false) }

    val ripple = remember {
        Ripple(
            radius = radius,
            color = Color(0xFF96FDFD),
            accessoryColor = Color(0xFFD3FFFD),
        )
    }

    val animation = remember(trigger) {
        RippleAnimation(durationMillis = 500)
            .also {
                ripple.setAnimation(it)
            }
    }

    animation.Start(trigger)

    MyCanvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { touchPosition ->
                    touchPos = touchPosition
                    trigger = !trigger
                    ripple.setAngle()
                }
            },
        backgroundColor = Color.Black
    ) {
        ripple.setCenter(touchPos)
        ripple.draw(it)
    }

}