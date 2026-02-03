package com.labs.systemdesignandroid.feature.sync.useCase

import androidx.work.WorkManager
import javax.inject.Inject

class CancelSyncWorkUseCase @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke() {
        workManager.cancelUniqueWork("movie_sync")
    }
}