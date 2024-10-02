package com.uuranus.compose.nature_effects.sample

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uuranus.compose.nature_effects.hang.PendulumEffect

@Composable
fun PendulumSample(
    modifier: Modifier = Modifier,
) {
    PendulumEffect(
        modifier = modifier
            .fillMaxWidth()
            .padding(100.dp)
            .aspectRatio(1f),
        initialAngle = 90f,
    )
}