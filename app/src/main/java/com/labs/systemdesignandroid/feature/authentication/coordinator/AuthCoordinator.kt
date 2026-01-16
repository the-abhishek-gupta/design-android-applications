package com.labs.systemdesignandroid.feature.authentication.coordinator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthCoordinator(
    private val isSignedIn: () -> Boolean,
    private val syncNow: suspend () -> Unit,
    private val logout: suspend () -> Unit,
    private val scope: CoroutineScope
) {
    sealed interface Event {
        data class NavigateToAuth(val returnRoute: String) : Event
        data class SignedIn(val returnRoute: String?) : Event
        object SignedOut : Event
    }

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    // One-shot action to execute after sign-in (e.g., "like", "bookmark", "purchase", etc.)
    private var pendingAction: (() -> Unit)? = null

    // Optional: if you want a one-shot task after sign-in (like syncing)
    private var pendingPostSignInTask: (suspend () -> Unit)? = null

    var returnRoute: String? by mutableStateOf(null)
        private set

    /**
     * Call this from any screen when an action requires auth.
     * If already signed-in → runs immediately.
     * Else → stores action, emits navigation event to Auth screen.
     */
    fun requireSignIn(
        currentRoute: String,
        afterSignIn: (() -> Unit)? = null
    ) {
        if (isSignedIn()) {
            afterSignIn?.invoke()
            return
        }
        pendingAction = afterSignIn

        //  Ensure sync runs after login once
        pendingPostSignInTask = { syncNow() }

        returnRoute = currentRoute
        _events.tryEmit(Event.NavigateToAuth(returnRoute = currentRoute))
    }

    /**
     * Call when Firebase sign-in succeeds.
     * 1) runs syncNow()
     * 2) runs pending action once
     * 3) emits SignedIn event
     */
    fun onSignedIn() {
        val route = returnRoute

        scope.launch {
            //sync catalog + pull remote user state + push pending
            runCatching { pendingPostSignInTask?.invoke() }
            pendingPostSignInTask = null

            // run the one-shot action (UI thing) after sync
            pendingAction?.invoke()
            pendingAction = null

            _events.emit(Event.SignedIn(returnRoute = route))
        }
    }

    /**
     * Call when user taps "Logout"
     * 1) signs out
     * 2) clears local user state
     * 3) emits SignedOut
     */
    fun onLogout() {
        scope.launch {
            runCatching { logout() }
            clear()
            _events.emit(Event.SignedOut)
        }
    }

    fun clear() {
        pendingAction = null
        pendingPostSignInTask = null
        returnRoute = null
    }
}