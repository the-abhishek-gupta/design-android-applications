package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.repository.MovieRepository
import com.labs.systemdesignandroid.feature.sync.useCase.EnqueueSyncWorkUseCase
import javax.inject.Inject

class RateMovieUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val enqueueSyncWork: EnqueueSyncWorkUseCase
) {
    suspend operator fun invoke(movieId: Int, rating: Int) {
        repository.rateMovie(movieId, rating.coerceIn(0, 5))
        enqueueSyncWork()
    }
}
