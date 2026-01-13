package com.labs.systemdesignandroid.data.remote

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.serialization.Serializable

@Serializable
data class MovieRemoteResponse(
    val movies: List<MovieModel>
)
