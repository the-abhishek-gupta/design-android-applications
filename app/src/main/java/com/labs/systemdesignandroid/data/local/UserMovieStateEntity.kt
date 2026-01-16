package com.labs.systemdesignandroid.data.local

import androidx.room.Entity

@Entity(
    tableName = "user_movie_state",
    primaryKeys = ["movieId"]
)
data class UserMovieStateEntity(
    val movieId: Int,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val userRating: Int = 0,          // 0..5
    val pendingSync: Boolean = false,
    val remoteUpdatedAt: Long = 0L    // server timestamp millis
)
