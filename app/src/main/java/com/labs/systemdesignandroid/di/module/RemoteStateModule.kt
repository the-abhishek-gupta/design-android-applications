package com.labs.systemdesignandroid.di.module

import com.labs.systemdesignandroid.data.remote.FirestoreUserMovieStateRemoteDataSource
import com.labs.systemdesignandroid.data.remote.UserMovieStateRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteStateModule {

    @Binds
    @Singleton
    abstract fun bindUserMovieStateRemoteDataSource(
        impl: FirestoreUserMovieStateRemoteDataSource
    ): UserMovieStateRemoteDataSource
}
