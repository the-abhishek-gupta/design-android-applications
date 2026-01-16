package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel

data class MovieWithUserStateRow(
    val id: Int,
    val name: String,
    val genres: String,
    val durationMinutes: Int,
    val rating: Double,
    val year: Int,
    val imageUrl: String,
    val description: String,

    val isFavorite: Boolean?,
    val isInWatchlist: Boolean?,
    val userRating: Int?,
    val pendingSync: Boolean?,
    val remoteUpdatedAt: Long?
)

fun MovieWithUserStateRow.toDomain(): MovieModel =
    MovieModel(
        id = id,
        name = name,
        genres = genres.split(",").map { it.trim() }.filter { it.isNotBlank() },
        durationMinutes = durationMinutes,
        rating = rating,
        year = year,
        imageUrl = imageUrl,
        description = description,
        isFavorite = isFavorite ?: false,
        isInWatchlist = isInWatchlist ?: false,
        userRating = userRating ?: 0,
        pendingSync = pendingSync ?: false,
        remoteUpdatedAt = remoteUpdatedAt ?: 0L
    )
