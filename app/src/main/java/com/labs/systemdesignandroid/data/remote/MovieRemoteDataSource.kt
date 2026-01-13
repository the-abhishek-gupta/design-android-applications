package com.labs.systemdesignandroid.data.remote

import com.labs.systemdesignandroid.domain.model.MovieModel

interface MovieRemoteDataSource {
    suspend fun fetchMovies(): List<MovieModel>
}
