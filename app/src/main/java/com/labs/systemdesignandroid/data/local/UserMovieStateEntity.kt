package com.labs.systemdesignandroid.data.local

import androidx.room.Entity
import com.labs.systemdesignandroid.domain.MovieReaction

@Entity(
    tableName = "user_movie_state",
    primaryKeys = ["userId", "movieId"]
)
data class UserMovieStateEntity(
    val userId: String,
    val movieId: Int,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val userRating: Int = 0,          // 0..5
    val reactions: Set<MovieReaction> = emptySet(),
    val pendingSync: Boolean = false,
    val remoteUpdatedAt: Long = 0L    // server timestamp millis
)
