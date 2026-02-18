package com.labs.systemdesignandroid.data.local

import androidx.room.TypeConverter
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun fromReactionList(list: Set<MovieReaction>): String {
        return list.joinToString(separator = ",") { it.name }
    }

    @TypeConverter
    fun toReactionList(value: String): Set<MovieReaction> {
        return value.split(",").mapNotNull {
            try {
                MovieReaction.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null // Handle unknown enum values gracefully
            }
        }.toSet()
    }
}
