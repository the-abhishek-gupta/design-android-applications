package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    fun observeMovies(userId: String): Flow<List<MovieModel>>

    suspend fun upsertCatalog(movies: List<MovieCatalogEntity>)

    suspend fun getUserState(userId: String, movieId: Int): UserMovieStateEntity?
    suspend fun upsertUserState(state: UserMovieStateEntity)

    suspend fun getPendingUserState(userId: String): List<UserMovieStateEntity>
    suspend fun markSynced(userId: String, movieId: Int, remoteUpdatedAt: Long)

    suspend fun clearUserState(userId: String)

    suspend fun applyRemoteState(
        userId: String,
        movieId: Int,
        favorite: Boolean,
        watchlist: Boolean,
        rating: Int,
        reactions: Set<MovieReaction>,
        remoteUpdatedAt: Long
    )
}
