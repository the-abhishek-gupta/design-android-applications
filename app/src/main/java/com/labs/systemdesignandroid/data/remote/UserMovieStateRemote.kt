package com.labs.systemdesignandroid.data.remote

import com.labs.systemdesignandroid.domain.MovieReaction

data class UserMovieStateRemote(
    val favorite: Boolean = false,
    val watchlist: Boolean = false,
    val rating: Int = 0,
    val reactions: Set<MovieReaction> = emptySet(),
    val updatedAtMillis: Long = 0L
)
