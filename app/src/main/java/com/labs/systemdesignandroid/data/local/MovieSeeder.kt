package com.labs.systemdesignandroid.data.local

import android.content.Context
import com.labs.systemdesignandroid.domain.model.MovieModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject
import javax.inject.Singleton

interface MovieSeeder {
    suspend fun seed(database: AppDatabase)
}
@Singleton
class JsonMovieSeeder @Inject constructor(
    private val json: Json,
    @ApplicationContext private val context: Context
) : MovieSeeder {

    override suspend fun seed(database: AppDatabase) {
        val dao = database.movieDao()
        val jsonString = context
            .assets
            .open("movies.json")
            .bufferedReader()
            .use { it.readText() }

        val root = json.parseToJsonElement(jsonString).jsonObject
        val movies = json.decodeFromJsonElement<List<MovieModel>>(
            root["movies"]!!
        )

        dao.insertMovies(movies.map { it.toEntity() })
    }
}
