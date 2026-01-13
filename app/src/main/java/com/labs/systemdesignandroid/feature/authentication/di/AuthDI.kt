package com.labs.systemdesignandroid.feature.authentication.di

import androidx.compose.runtime.compositionLocalOf
import com.labs.systemdesignandroid.feature.authentication.coordinator.AuthCoordinator

val LocalAuthCoordinator = compositionLocalOf<AuthCoordinator> {
    error("AuthCoordinator not provided")
}
