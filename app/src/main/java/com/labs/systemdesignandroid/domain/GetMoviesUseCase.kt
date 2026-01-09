package com.labs.systemdesignandroid.domain

import com.labs.systemdesignandroid.data.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getMovies()
    }
}
