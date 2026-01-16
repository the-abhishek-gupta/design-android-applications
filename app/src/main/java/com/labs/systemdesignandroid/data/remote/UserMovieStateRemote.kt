package com.labs.systemdesignandroid.data.remote

data class UserMovieStateRemote(
    val favorite: Boolean = false,
    val watchlist: Boolean = false,
    val rating: Int = 0,
    val updatedAtMillis: Long = 0L
)
