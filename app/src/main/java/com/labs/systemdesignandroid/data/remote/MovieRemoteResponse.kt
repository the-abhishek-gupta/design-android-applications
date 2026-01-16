package com.labs.systemdesignandroid.data.remote

import com.labs.systemdesignandroid.data.local.MovieCatalogEntity
import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.serialization.Serializable

@Serializable
data class MovieRemoteResponse(
    val movies: List<MovieRemoteModel>
)


@Serializable
data class MovieRemoteModel(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val durationMinutes: Int,
    val rating: Double,
    val year: Int,
    val imageUrl: String,
    val description: String
)

fun MovieRemoteModel.toCatalogEntity() = MovieCatalogEntity(
    id = id,
    name = name,
    genres = genres.joinToString(","),
    durationMinutes = durationMinutes,
    rating = rating,
    year = year,
    imageUrl = imageUrl,
    description = description
)