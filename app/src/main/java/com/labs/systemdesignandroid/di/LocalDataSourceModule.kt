package com.labs.systemdesignandroid.di

import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import com.labs.systemdesignandroid.data.local.MovieLocalDataSourceImpl
import com.labs.systemdesignandroid.data.remote.FakeMovieRemoteDataSource
import com.labs.systemdesignandroid.data.remote.MovieRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindMovieLocalDataSource(
        impl: MovieLocalDataSourceImpl
    ): MovieLocalDataSource
}
