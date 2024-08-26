package com.uuranus.compose.nature_effects.util

import kotlin.math.PI

fun Double.toDegree(): Double {
    return this * (180.0 / PI)
}

fun Double.toRadian(): Double {
    return this * (PI / 180.0)
}

fun Float.toDegree(): Float {
    return this * (180f / PI).toFloat()
}

fun Float.toRadian(): Float {
    return this * (PI / 180f).toFloat()
}

fun Float.to360Radian(): Float {
    return if (this < 0) {
        PI.toFloat() + this + PI.toFloat()
    } else {
        this
    }
}

data class Point(
    val x: Float,
    val y: Float,
)