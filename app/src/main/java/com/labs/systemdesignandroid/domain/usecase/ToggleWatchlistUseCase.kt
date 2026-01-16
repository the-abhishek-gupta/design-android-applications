package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import com.labs.systemdesignandroid.feature.sync.useCase.EnqueueSyncWorkUseCase
import javax.inject.Inject

class ToggleWatchlistUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val enqueueSyncWork: EnqueueSyncWorkUseCase
) {
    suspend operator fun invoke(movieId: Int) {
        repository.toggleWatchlist(movieId)
        enqueueSyncWork()
    }
}
