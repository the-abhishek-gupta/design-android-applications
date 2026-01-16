package com.labs.systemdesignandroid.domain.repository

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeMovies(): Flow<List<MovieModel>>
    suspend fun refreshCatalog()
    suspend fun toggleFavorite(movieId: Int)
    suspend fun toggleWatchlist(movieId: Int)
    suspend fun rateMovie(movieId: Int, rating: Int)
    suspend fun pushPending()
    suspend fun syncNow()
    fun observeRemoteSync(): Flow<Unit>
}
