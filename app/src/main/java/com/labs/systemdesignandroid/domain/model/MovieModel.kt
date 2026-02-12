package com.labs.systemdesignandroid.domain.model

import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val durationMinutes: Int,
    val rating: Double,
    val year: Int,
    val imageUrl: String,
    val description: String,

    val isFavorite: Boolean,
    val isInWatchlist: Boolean,
    val userRating: Int,
    val userReactions: Set<MovieReaction>,
    val pendingSync: Boolean,
    val remoteUpdatedAt: Long
)