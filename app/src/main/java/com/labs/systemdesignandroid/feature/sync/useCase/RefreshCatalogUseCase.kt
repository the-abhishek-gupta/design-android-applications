package com.labs.systemdesignandroid.feature.sync.useCase

import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class RefreshCatalogUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() {
        repository.refreshCatalog()
    }
}
