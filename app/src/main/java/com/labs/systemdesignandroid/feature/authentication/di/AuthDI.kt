package com.labs.systemdesignandroid.feature.authentication.di

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.labs.systemdesignandroid.feature.authentication.coordinator.AuthCoordinator

val LocalAuthCoordinator = staticCompositionLocalOf<AuthCoordinator> {
    error("AuthCoordinator not provided")
}
