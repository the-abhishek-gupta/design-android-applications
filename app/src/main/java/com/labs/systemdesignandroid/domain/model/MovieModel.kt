package com.labs.systemdesignandroid.domain.model

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
    val pendingSync: Boolean,
    val remoteUpdatedAt: Long
)