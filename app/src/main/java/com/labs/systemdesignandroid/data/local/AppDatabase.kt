package com.labs.systemdesignandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MovieEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Add isInWatchlist columns to the movies table
                db.execSQL("ALTER TABLE movies ADD COLUMN isInWatchlist INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
