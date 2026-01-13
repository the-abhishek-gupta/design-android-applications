package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class RefreshMoviesUseCase @Inject constructor(
    private val repo: MovieRepository
) {
    suspend operator fun invoke() = repo.refresh()
}
