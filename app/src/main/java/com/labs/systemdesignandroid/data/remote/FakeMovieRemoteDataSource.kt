package com.labs.systemdesignandroid.data.remote

import android.content.Context
import com.labs.systemdesignandroid.domain.model.MovieModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeMovieRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context, private val json: Json
) : MovieRemoteDataSource {
    override suspend fun fetchMovies(): List<MovieModel> {
        delay(1000) // simulate network
        val jsonString = context.assets.open("movies.json").bufferedReader().use { it.readText() }

        val response = json.decodeFromString<MovieRemoteResponse>(jsonString)
        return response.movies.map { it.toDomain() }
    }
}

private fun MovieModel.toDomain(): MovieModel = MovieModel(
    id = id,
    name = name,
    genres = genres,
    description = description,
    rating = rating,
    year = year,
    isFavorite = isFavorite,
    isInWatchlist = isInWatchlist,
    durationMinutes = durationMinutes,
    imageUrl = imageUrl
)

