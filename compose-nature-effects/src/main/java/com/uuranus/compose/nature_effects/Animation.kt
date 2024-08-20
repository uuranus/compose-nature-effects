package com.uuranus.compose.nature_effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Animation {

    abstract fun getWaveOffsetY(): Float

    abstract fun getWaterHeight(): Float
}