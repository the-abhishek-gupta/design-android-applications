package com.labs.systemdesignandroid.data

import com.labs.systemdesignandroid.domain.MovieReaction

data class EmojiParticle(
    val reaction: MovieReaction,
    val baseX: Float,
    var x: Float,          // now in PX
    var y: Float,          // now in PX
    var rotation: Float,
    var velocityX: Float,
    var velocityY: Float,
    val swingAmplitude: Float,
    val swingFrequency: Float,
    val scale: Float,
    val particleIndex: Int
)