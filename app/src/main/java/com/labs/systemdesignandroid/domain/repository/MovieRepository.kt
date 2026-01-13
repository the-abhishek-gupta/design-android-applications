package com.labs.systemdesignandroid.domain.repository

import com.labs.systemdesignandroid.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
    suspend fun updateMovie(movie: Movie)
}
