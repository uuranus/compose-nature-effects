package com.uuranus.compose.nature_effects.util

import kotlin.math.PI

fun Double.toDegree(): Double {
    return this * (180.0 / PI)
}

fun Double.toRadian(): Double {
    return this * (PI / 180.0)
}

data class Point(
    val x: Float,
    val y: Float,
)