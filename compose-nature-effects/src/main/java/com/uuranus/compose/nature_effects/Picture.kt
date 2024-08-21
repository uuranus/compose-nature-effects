package com.uuranus.compose.nature_effects

import androidx.compose.ui.graphics.drawscope.DrawScope

abstract class Picture {

    open fun draw(drawScope: DrawScope) {}
}