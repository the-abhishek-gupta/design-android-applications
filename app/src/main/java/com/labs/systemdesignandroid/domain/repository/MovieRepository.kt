package com.labs.systemdesignandroid.domain.repository

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeMovies(): Flow<List<MovieModel>>
    suspend fun refresh()
    suspend fun toggleFavorite(movieId: Int)
    suspend fun toggleWatchlist(movieId: Int)
}
