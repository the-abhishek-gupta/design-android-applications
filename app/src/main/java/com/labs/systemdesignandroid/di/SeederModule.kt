package com.labs.systemdesignandroid.di

import android.content.Context
import androidx.room.Room
import com.labs.systemdesignandroid.data.local.AppDatabase
import com.labs.systemdesignandroid.data.local.JsonMovieSeeder
import com.labs.systemdesignandroid.data.local.MovieDao
import com.labs.systemdesignandroid.data.local.MovieSeeder
import com.labs.systemdesignandroid.data.repository.MovieRepositoryImpl
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

