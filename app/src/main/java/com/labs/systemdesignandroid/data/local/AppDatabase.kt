package com.labs.systemdesignandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.labs.systemdesignandroid.di.ApplicationScope
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    class AppDatabaseCallback @Inject constructor(
        private val seeder: MovieSeeder,
        @ApplicationScope private val appScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        lateinit var databaseProvider: () -> AppDatabase

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            appScope.launch {
                seeder.seed(databaseProvider())
            }
        }
    }
}

