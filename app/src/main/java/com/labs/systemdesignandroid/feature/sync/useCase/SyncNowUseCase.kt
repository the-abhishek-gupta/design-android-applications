package com.labs.systemdesignandroid.feature.sync.useCase

import javax.inject.Inject

class SyncNowUseCase @Inject constructor(
    private val refreshCatalog: RefreshCatalogUseCase,
    private val pullRemoteUserState: PullRemoteUserStateUseCase,
    private val pushPendingChanges: PushPendingChangesUseCase
) {
    suspend operator fun invoke() {
        refreshCatalog()
        // pull first so you get latest state quickly
        pullRemoteUserState()
        // then push local pending
        pushPendingChanges()
    }
}
