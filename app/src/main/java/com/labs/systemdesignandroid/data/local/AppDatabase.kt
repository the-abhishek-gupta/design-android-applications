package com.labs.systemdesignandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.labs.systemdesignandroid.di.ApplicationScope
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [MovieCatalogEntity::class, UserMovieStateEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    class AppDatabaseCallback @Inject constructor(
        private val seeder: MovieSeeder,
        @ApplicationScope private val appScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // Set this from your Room builder (Provider-based setup)
        var databaseProvider: (() -> AppDatabase)? = null

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            appScope.launch {
                val database = databaseProvider?.invoke() ?: return@launch

                // Seed only if catalog is empty
                val dao = database.movieDao()
                val count = dao.getCatalogCount()
                if (count == 0) {
                    seeder.seed(database)
                }
            }
        }
    }
}
