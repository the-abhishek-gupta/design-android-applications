package com.labs.systemdesignandroid.feature.authentication

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface AuthUserProvider {
    fun uidOrNull(): String?
    fun isSignedIn(): Boolean
}

class FirebaseAuthUserProvider @Inject constructor(
    private val auth: FirebaseAuth
) : AuthUserProvider {
    override fun uidOrNull(): String? = auth.currentUser?.uid

    override fun isSignedIn(): Boolean = auth.currentUser != null
}
