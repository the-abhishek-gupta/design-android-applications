package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    fun observeMovies(): Flow<List<MovieModel>>
    suspend fun upsertCatalog(movies: List<MovieCatalogEntity>)
    suspend fun getUserState(movieId: Int): UserMovieStateEntity?
    suspend fun upsertUserState(state: UserMovieStateEntity)
    suspend fun getPendingUserState(): List<UserMovieStateEntity>
    suspend fun markSynced(movieId: Int, remoteUpdatedAt: Long)
    suspend fun clearUserState()

    suspend fun applyRemoteState(
        movieId: Int,
        favorite: Boolean,
        watchlist: Boolean,
        rating: Int,
        remoteUpdatedAt: Long
    )
}
