package com.labs.systemdesignandroid.di.module

import com.labs.systemdesignandroid.feature.authentication.AuthUserProvider
import com.labs.systemdesignandroid.feature.authentication.FirebaseAuthUserProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthUserProvider(
        impl: FirebaseAuthUserProvider
    ): AuthUserProvider
}
