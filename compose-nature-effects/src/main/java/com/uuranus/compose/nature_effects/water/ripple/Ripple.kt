package com.uuranus.compose.nature_effects.water.ripple

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.uuranus.compose.nature_effects.Picture
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Ripple(
    private val radius: Float,
    private val color: Color,
    private val accessoryColor: Color,
) : Picture() {
    private lateinit var animation: RippleAnimation

    private var centerOffset by mutableStateOf(Offset.Unspecified)

    private var rhombusStartAngle = 0.0

    override fun draw(drawScope: DrawScope) {
        if (centerOffset == Offset.Unspecified) return

        var currentAngle = rhombusStartAngle

        repeat(3) {
            drawRhombus(drawScope, currentAngle)

            currentAngle += PI * 2 / 3
        }

        drawRing(drawScope)
    }

    private fun drawRing(drawScope: DrawScope) {

        val radius = radius * animation.getCurrentRingRadiusScale()
        val ringWidth = radius / 8f

        val path = Path().apply {

            addOval(
                Rect(
                    left = centerOffset.x - radius,
                    top = centerOffset.y - radius,
                    right = centerOffset.x + radius,
                    bottom = centerOffset.y + radius
                )
            )

            op(
                path1 = this,
                path2 = Path().apply {
                    addOval(
                        Rect(
                            left = centerOffset.x - radius + ringWidth,
                            top = centerOffset.y - radius + ringWidth,
                            right = centerOffset.x + radius - ringWidth,
                            bottom = centerOffset.y + radius - ringWidth
                        )
                    )
                },
                operation = PathOperation.Difference
            )

        }

        drawScope.drawPath(
            path = path,
            brush = Brush.radialGradient(
                colors = listOf(color, Color.Transparent),
                center = centerOffset
            ),
            alpha = animation.getCurrentRingAlpha()
        )
    }

    private fun drawRhombus(drawScope: DrawScope, angle: Double) {
        val radius = radius * animation.getCurrentRhombusRadiusScale()

        val size = Size(radius / 3f, radius / 3f) * animation.getCurrentRhombusScale()

        val path = Path()
        val centerX = centerOffset.x + radius * cos(angle).toFloat()
        val centerY = centerOffset.y - radius * sin(angle).toFloat()

        val top = Offset(
            centerX + size.height / 2f * cos(angle).toFloat(),
            centerY - size.height / 2f * sin(angle).toFloat()
        )

        val bottom = Offset(
            centerX + size.height / 2f * cos(angle + PI).toFloat(),
            centerY - size.height / 2f * sin(angle + PI).toFloat()
        )

        val perpendicularAngle = angle + Math.PI / 2

        val start = Offset(
            centerX + size.width / 2f * cos(perpendicularAngle).toFloat(),
            centerY - size.width / 2f * sin(perpendicularAngle).toFloat()
        )

        val end = Offset(
            centerX + size.width / 2f * cos(perpendicularAngle + PI).toFloat(),
            centerY - size.width / 2f * sin(perpendicularAngle + PI).toFloat()
        )

        path.apply {

            moveTo(start.x, start.y)

            lineTo(top.x, top.y)
            lineTo(end.x, end.y)
            lineTo(bottom.x, bottom.y)

            close()
        }

        drawScope.drawPath(
            path = path,
            color = accessoryColor,
        )
    }

    fun setAnimation(animation: RippleAnimation) {
        this.animation = animation
    }

    fun setCenter(offset: Offset) {
        centerOffset = offset
    }

    fun setAngle() {
        rhombusStartAngle = Random.nextDouble() * PI * 2
    }
}