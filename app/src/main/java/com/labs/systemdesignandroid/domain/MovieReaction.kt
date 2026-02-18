package com.labs.systemdesignandroid.domain

import kotlinx.serialization.Serializable

enum class MovieReaction(val emoji: String) {
    LIKE("\uD83D\uDC4D"),
    HAHA("\uD83D\uDE02"),
    SAD("\uD83D\uDE22"),
    ANGRY("\uD83D\uDE21"),
    FIRE("🔥");
    companion object {
        fun safeValueOf(name: String): MovieReaction? =
            try {
                valueOf(name)
            } catch (e: IllegalArgumentException) {
                null
            }
    }
}