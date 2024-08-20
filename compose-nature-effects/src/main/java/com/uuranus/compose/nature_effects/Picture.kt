package com.uuranus.compose.nature_effects

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

abstract class Picture(
    val offset: Offset = Offset.Zero,
    open val color: Color,
    val animation: Animation,
    val path: Path = Path(),
) {

    open fun draw(drawScope: DrawScope) {}
}