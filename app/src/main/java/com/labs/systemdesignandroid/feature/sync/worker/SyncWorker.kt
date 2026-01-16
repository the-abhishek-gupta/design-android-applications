package com.labs.systemdesignandroid.feature.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.labs.systemdesignandroid.feature.sync.useCase.PushPendingChangesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val pushPendingChanges: PushPendingChangesUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            pushPendingChanges()
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}
