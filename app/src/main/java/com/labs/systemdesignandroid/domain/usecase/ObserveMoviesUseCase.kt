package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMoviesUseCase @Inject constructor(
    private val repo: MovieRepository
) {
    operator fun invoke(): Flow<List<MovieModel>> =
        repo.observeMovies()
}
