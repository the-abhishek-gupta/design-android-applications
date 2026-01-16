package com.labs.systemdesignandroid.data.remote

import android.content.Context
import com.labs.systemdesignandroid.data.local.MovieCatalogEntity
import com.labs.systemdesignandroid.domain.model.MovieModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeMovieRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) : MovieRemoteDataSource {

    override suspend fun fetchCatalog(): List<MovieCatalogEntity> {
        delay(1000)
        val jsonString = context.assets.open("movies.json")
            .bufferedReader().use { it.readText() }

        val response = json.decodeFromString<MovieRemoteResponse>(jsonString)
        return response.movies.map { it.toCatalogEntity() }
    }
}

fun MovieModel.toCatalogEntity() = MovieCatalogEntity(
    id = id,
    name = name,
    genres = genres.joinToString(","),
    durationMinutes = durationMinutes,
    rating = rating,
    year = year,
    imageUrl = imageUrl,
    description = description
)

