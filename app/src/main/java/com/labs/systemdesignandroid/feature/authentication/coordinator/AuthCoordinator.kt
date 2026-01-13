package com.labs.systemdesignandroid.feature.authentication.coordinator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthCoordinator(
    private val isSignedIn: () -> Boolean
) {
    sealed interface Event {
        data class NavigateToAuth(val returnRoute: String) : Event
        object SignedIn : Event
    }

    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    // One-shot action to execute after sign-in (e.g., "like", "bookmark", "purchase", etc.)
    private var pendingAction: (() -> Unit)? = null

    // Where to return after sign-in
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
        returnRoute = currentRoute
        _events.tryEmit(Event.NavigateToAuth(returnRoute = currentRoute))
    }

    /**
     * Call when Firebase sign-in succeeds.
     * Executes pending action once.
     */
    fun onSignedIn() {
        pendingAction?.invoke()
        pendingAction = null
        _events.tryEmit(Event.SignedIn)
    }

    fun clear() {
        pendingAction = null
        returnRoute = null
    }
}