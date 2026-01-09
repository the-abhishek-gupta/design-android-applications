package com.labs.systemdesignandroid.di

import android.content.Context
import androidx.room.Room
import com.labs.systemdesignandroid.data.MovieRepository
import com.labs.systemdesignandroid.data.MovieRepositoryImpl
import com.labs.systemdesignandroid.data.local.AppDatabase
import com.labs.systemdesignandroid.data.local.MovieDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "movie_database"
            ).build()
        }

        @Provides
        fun provideMovieDao(database: AppDatabase): MovieDao {
            return database.movieDao()
        }
    }
}
