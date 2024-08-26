package com.uuranus.compose.nature_effects

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.DrawScope

open class Picture {

    open fun draw(drawScope: DrawScope) {}

    @Composable
    open fun Draw(boxScope: BoxScope) {
    }
}