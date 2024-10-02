package com.uuranus.compose.nature_effects.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.uuranus.compose.nature_effects.R
import com.uuranus.compose.nature_effects.wind.WindBlownDiagonalEffect
import com.uuranus.compose.nature_effects.wind.WindBlownEffect

@Composable
fun WindSample(
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF3A3A3A))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        WindBlownDiagonalEffect(
            R.drawable.leaf
        )
        WindBlownEffect(
            backgroundColor = Color.Transparent,
            lightColor = Color(0xFFFdFF99)
        )
    }
}