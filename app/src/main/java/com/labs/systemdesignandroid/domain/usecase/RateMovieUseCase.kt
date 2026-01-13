package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class RateMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, rating: Int) {
        repository.rateMovie(movieId, rating.coerceIn(0, 5))
    }
}
