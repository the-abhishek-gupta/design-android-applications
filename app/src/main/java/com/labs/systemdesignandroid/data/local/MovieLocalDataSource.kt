package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    fun observeMovies(): Flow<List<MovieModel>>
    suspend fun getMovieById(id: Int): MovieModel?
    suspend fun upsert(movies: List<MovieModel>)
}
