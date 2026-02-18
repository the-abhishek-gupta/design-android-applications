package com.labs.systemdesignandroid.data.remote

import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.coroutines.flow.Flow

interface UserMovieStateRemoteDataSource {
    suspend fun upsert(uid: String, movieId: Int, favorite: Boolean, watchlist: Boolean, rating: Int, reaction : Set<MovieReaction>)
    suspend fun get(uid: String, movieId: Int): UserMovieStateRemote?
    fun listenAll(uid: String): Flow<Map<Int, UserMovieStateRemote>>
    suspend fun fetchAll(uid: String): Map<Int, UserMovieStateRemote>
}
