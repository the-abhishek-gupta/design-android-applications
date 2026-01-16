package com.labs.systemdesignandroid.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // Observe catalog + user state combined for UI
    @Query("""
        SELECT
            m.id, m.name, m.genres, m.durationMinutes, m.rating, m.year, m.imageUrl, m.description,
            s.isFavorite, s.isInWatchlist, s.userRating, s.pendingSync, s.remoteUpdatedAt
        FROM movies m
        LEFT JOIN user_movie_state s ON s.movieId = m.id
    """)
    fun observeMoviesWithState(): Flow<List<MovieWithUserStateRow>>

    // Catalog upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCatalog(movies: List<MovieCatalogEntity>)

    // Get catalog item if you need it
    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getCatalogById(id: Int): MovieCatalogEntity?

    // User-state helpers
    @Query("SELECT * FROM user_movie_state WHERE movieId = :movieId LIMIT 1")
    suspend fun getUserState(movieId: Int): UserMovieStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserState(state: UserMovieStateEntity)

    @Query("SELECT * FROM user_movie_state WHERE pendingSync = 1")
    suspend fun getPendingUserState(): List<UserMovieStateEntity>

    @Query("""
        UPDATE user_movie_state
        SET pendingSync = 0, remoteUpdatedAt = :remoteUpdatedAt
        WHERE movieId = :movieId
    """)
    suspend fun markSynced(movieId: Int, remoteUpdatedAt: Long)

    @Query("""
        UPDATE user_movie_state
        SET isFavorite = :favorite,
            isInWatchlist = :watchlist,
            userRating = :rating,
            pendingSync = 0,
            remoteUpdatedAt = :remoteUpdatedAt
        WHERE movieId = :movieId
    """)
    suspend fun applyRemoteState(
        movieId: Int,
        favorite: Boolean,
        watchlist: Boolean,
        rating: Int,
        remoteUpdatedAt: Long
    )
    @Query("DELETE FROM user_movie_state")
    suspend fun clearUserState()

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getCatalogCount(): Int

}
