package com.labs.systemdesignandroid.feature.authentication.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthRepository(
    private val appContext: Context,
    private val serverClientId: String
) {
    private val credentialManager = CredentialManager.create(appContext)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId) // IMPORTANT: Web client ID
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = appContext
        )

        val googleCred = GoogleIdTokenCredential.createFrom(result.credential.data)
        val idToken = googleCred.idToken

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential).await()
    }

    fun signOut() = auth.signOut()

    fun currentUser() = auth.currentUser
}
