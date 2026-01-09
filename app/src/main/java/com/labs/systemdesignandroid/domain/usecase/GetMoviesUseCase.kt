package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.Movie
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getMovies()
    }
}
