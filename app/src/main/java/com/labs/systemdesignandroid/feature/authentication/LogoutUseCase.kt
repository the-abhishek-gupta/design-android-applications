package com.labs.systemdesignandroid.feature.authentication

import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val local: MovieLocalDataSource,
    private val workManager: WorkManager
) {
    suspend operator fun invoke() {
        val uid = auth.currentUser?.uid
        if (uid != null) local.clearUserState(uid)
        workManager.cancelUniqueWork("movie_sync")
        auth.signOut()
    }
}
