package com.uuranus.compose.nature_effects.hang

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import com.uuranus.compose.nature_effects.Picture
import com.uuranus.compose.nature_effects.util.toDegree
import com.uuranus.compose.nature_effects.util.toRadian
import kotlin.math.cos
import kotlin.math.sin

class Pendulum(
    private val size: IntSize,
    private val animation: PendulumAnimation,
) : Picture() {

    @Composable
    override fun Draw(boxScope: BoxScope) {
        val iconSize = with(LocalDensity.current) {
            minOf(size.width, size.height).toDp()
        }

        val radius = with(LocalDensity.current) {
            iconSize.toPx() / 2
        }

        Box(
            modifier = Modifier
                .size(iconSize)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                modifier = androidx.compose.ui.Modifier
                    .size(iconSize)
                    .graphicsLayer {

                        val centerY = size.height / 2
                        val angleDegree = animation
                            .getCurrentRotationDegree()
                        val angleRadians = angleDegree.toRadian()

                        this.rotationZ = angleDegree

                        this.translationX = -radius * sin(angleRadians)
                        this.translationY = -centerY + radius * cos(angleRadians)

                    }
            )
        }

    }
}