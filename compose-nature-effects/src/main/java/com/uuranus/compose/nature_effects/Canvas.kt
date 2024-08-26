package com.uuranus.compose.nature_effects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun MyCanvas(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    shape: Shape = RectangleShape,
    content: (DrawScope) -> Unit,
) {

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clip(shape)
    ) {
        content(this)
    }
}



