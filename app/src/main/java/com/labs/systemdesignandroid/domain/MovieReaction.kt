package com.labs.systemdesignandroid.domain

import kotlinx.serialization.Serializable

@Serializable
enum class MovieReaction(val emoji: String) {
    LIKE("👍"),
    LOVE("❤️"),
    FIRE("🔥"),
    LAUGH("😂"),
    WOW("😮"),
    SAD("😢")
}