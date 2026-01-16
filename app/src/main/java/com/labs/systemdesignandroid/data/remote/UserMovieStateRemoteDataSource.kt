package com.labs.systemdesignandroid.data.remote

import kotlinx.coroutines.flow.Flow

interface UserMovieStateRemoteDataSource {
    suspend fun upsert(uid: String, movieId: Int, favorite: Boolean, watchlist: Boolean, rating: Int)
    suspend fun get(uid: String, movieId: Int): UserMovieStateRemote?
    fun listenAll(uid: String): Flow<Map<Int, UserMovieStateRemote>>
    suspend fun fetchAll(uid: String): Map<Int, UserMovieStateRemote>
}
