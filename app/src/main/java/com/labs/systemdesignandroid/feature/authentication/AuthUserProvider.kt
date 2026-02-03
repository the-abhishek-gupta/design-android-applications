package com.labs.systemdesignandroid.feature.authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface AuthUserProvider {
    val uidFlow: StateFlow<String?>
    fun uidOrNull(): String?
    fun isSignedIn(): Boolean
}

class FirebaseAuthUserProvider @Inject constructor(
    private val auth: FirebaseAuth
) : AuthUserProvider {

    private val _uidFlow = MutableStateFlow(auth.currentUser?.uid)
    override val uidFlow: StateFlow<String?> = _uidFlow

    private val listener = FirebaseAuth.AuthStateListener { a ->
        _uidFlow.value = a.currentUser?.uid
    }

    init {
        auth.addAuthStateListener(listener)
    }

    override fun uidOrNull(): String? = auth.currentUser?.uid

    override fun isSignedIn(): Boolean = auth.currentUser != null
}
