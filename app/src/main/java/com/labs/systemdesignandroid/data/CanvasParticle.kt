package com.labs.systemdesignandroid.data

data class CanvasParticle(
    val emoji: String,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var rotation: Float,
    var angularVelocity: Float,
    var scale: Float,
    var alpha: Float,
    var life: Float
)
