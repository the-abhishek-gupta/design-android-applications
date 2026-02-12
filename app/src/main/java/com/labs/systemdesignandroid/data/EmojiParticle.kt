package com.labs.systemdesignandroid.data

import com.labs.systemdesignandroid.domain.MovieReaction

data class EmojiParticle(
    val reaction: MovieReaction,
    val x: Float,
    val targetX: Float,
    val targetY: Float,
    val rotation: Float,
    val velocityX: Float = 0f,
    val velocityY: Float = 500f,
    val swingAmplitude: Float = 40f,
    val swingFrequency: Float = 2f,
    val scale: Float = 1f,
    val particleIndex: Int
)