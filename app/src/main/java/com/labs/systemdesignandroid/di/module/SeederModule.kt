package com.labs.systemdesignandroid.di.module

import com.labs.systemdesignandroid.data.local.JsonMovieSeeder
import com.labs.systemdesignandroid.data.local.MovieSeeder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SeederModule {

    @Binds
    @Singleton
    abstract fun bindMovieSeeder(
        impl: JsonMovieSeeder
    ): MovieSeeder
}