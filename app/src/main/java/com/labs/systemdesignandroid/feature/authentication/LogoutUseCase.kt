package com.labs.systemdesignandroid.feature.authentication

import com.google.firebase.auth.FirebaseAuth
import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val local: MovieLocalDataSource
) {
    suspend operator fun invoke() {
        auth.signOut()
        local.clearUserState()
    }
}
