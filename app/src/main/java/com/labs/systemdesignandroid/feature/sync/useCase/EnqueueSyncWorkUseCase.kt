package com.labs.systemdesignandroid.feature.sync.useCase

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.labs.systemdesignandroid.feature.sync.worker.SyncWorker
import javax.inject.Inject

class EnqueueSyncWorkUseCase @Inject constructor(
    private val workManager: WorkManager
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            "movie_sync",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}
