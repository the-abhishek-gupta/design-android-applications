package com.labs.systemdesignandroid.domain

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val durationMinutes: Int,
    val rating: Double,
    val year: Int,
    val imageUrl: String,
    val description: String,
    val isFavorite: Boolean = false,
)
