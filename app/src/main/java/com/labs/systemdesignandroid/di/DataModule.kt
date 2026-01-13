package com.labs.systemdesignandroid.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.labs.systemdesignandroid.data.local.AppDatabase
import com.labs.systemdesignandroid.data.local.MovieDao
import com.labs.systemdesignandroid.data.repository.MovieRepositoryImpl
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository

    companion object {

        @Provides
        @Singleton
        fun provideAppDatabase(
            app: Application,
            callbackProvider: Provider<AppDatabase.AppDatabaseCallback>
        ): AppDatabase {

            lateinit var database: AppDatabase

            database = Room.databaseBuilder(
                app,
                AppDatabase::class.java,
                "movie_database.db"
            )
                .addCallback(
                    callbackProvider.get().also { callback ->
                        // ✅ THIS NOW COMPILES
                        callback.databaseProvider = { database }
                    }
                )
                .build()

            return database
        }

        @Provides
        fun provideMovieDao(
            database: AppDatabase
        ): MovieDao = database.movieDao()
    }
}

