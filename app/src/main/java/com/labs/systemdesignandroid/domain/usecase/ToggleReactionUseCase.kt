package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.data.local.UserMovieStateEntity
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import com.labs.systemdesignandroid.domain.MovieReaction
import com.labs.systemdesignandroid.feature.sync.useCase.EnqueueSyncWorkUseCase
import javax.inject.Inject

class ToggleReactionUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val enqueueSyncWork: EnqueueSyncWorkUseCase
) {
    suspend operator fun invoke(movieId: Int, reaction: MovieReaction, isSelected: Boolean) {
        repository.toggleReaction(movieId,reaction, isSelected)
        enqueueSyncWork()
    }
}
