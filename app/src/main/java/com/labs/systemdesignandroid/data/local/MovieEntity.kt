package com.labs.systemdesignandroid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.labs.systemdesignandroid.domain.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val genres: String, // Comma separated
    val durationMinutes: Int,
    val rating: Double,
    val year: Int,
    val imageUrl: String,
    val description: String,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
)

fun MovieEntity.toDomain() = Movie(
    id = id,
    name = name,
    genres = genres.split(",").filter { it.isNotBlank() },
    durationMinutes = durationMinutes,
    rating = rating,
    year = year,
    imageUrl = imageUrl,
    description = description,
    isFavorite = isFavorite,
    isInWatchlist = isInWatchlist,
)

fun Movie.toEntity() = MovieEntity(
    id = id,
    name = name,
    genres = genres.joinToString(","),
    durationMinutes = durationMinutes,
    rating = rating,
    year = year,
    imageUrl = imageUrl,
    description = description,
    isFavorite = isFavorite,
    isInWatchlist = isInWatchlist,
)
