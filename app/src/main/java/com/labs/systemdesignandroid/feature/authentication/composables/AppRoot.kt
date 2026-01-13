package com.labs.systemdesignandroid.feature.authentication.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.labs.systemdesignandroid.MovieViewModel
import com.labs.systemdesignandroid.core.ui.composables.MainNavigation
import com.labs.systemdesignandroid.feature.authentication.coordinator.AuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.di.LocalAuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.repository.GoogleAuthRepository

@Composable
fun AppRoot(serverClientId: String, viewModel: MovieViewModel) {
    val context = LocalContext.current
    val authRepo = remember(serverClientId) {
        GoogleAuthRepository(context.applicationContext, serverClientId)
    }
    val coordinator = remember {
        AuthCoordinator(isSignedIn = { authRepo.currentUser() != null })
    }

    CompositionLocalProvider(LocalAuthCoordinator provides coordinator) {
        MainNavigation(viewModel = viewModel, authRepo = authRepo)
    }
}

