package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) =
        repository.toggleFavorite(movieId)
}
