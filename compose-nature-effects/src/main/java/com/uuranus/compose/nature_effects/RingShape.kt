package com.uuranus.compose.nature_effects

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class RingShape(
    private val ringWidth: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path()

        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2

        val ringWidthPx = with(density) {
            ringWidth.toPx()
        }

        val innerRadius = radius - ringWidthPx

        path.apply {

            addOval(
                Rect(
                    left = centerX - radius,
                    top = centerY - radius,
                    right = centerX + radius,
                    bottom = centerY + radius
                )
            )

            op(
                path1 = this,
                path2 = Path().apply {
                    addOval(
                        Rect(
                            left = centerX - innerRadius,
                            top = centerY - innerRadius,
                            right = centerX + innerRadius,
                            bottom = centerY + innerRadius
                        )
                    )
                },
                operation = PathOperation.Difference
            )

        }

        return Outline.Generic(path)

    }
}
