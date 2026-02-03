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

    // Observe catalog + current user's state
    @Query("""
        SELECT
            m.id, m.name, m.genres, m.durationMinutes, m.rating, m.year, m.imageUrl, m.description,
            s.isFavorite, s.isInWatchlist, s.userRating, s.pendingSync, s.remoteUpdatedAt
        FROM movies m
        LEFT JOIN user_movie_state s ON s.movieId = m.id AND s.userId = :userId
    """)
    fun observeMoviesWithState(userId: String): Flow<List<MovieWithUserStateRow>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCatalog(movies: List<MovieCatalogEntity>)

    // --- user state ---
    @Query("""
        SELECT * FROM user_movie_state 
        WHERE userId = :userId AND movieId = :movieId 
        LIMIT 1
    """)
    suspend fun getUserState(userId: String, movieId: Int): UserMovieStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserState(state: UserMovieStateEntity)

    @Query("SELECT * FROM user_movie_state WHERE userId = :userId AND pendingSync = 1")
    suspend fun getPendingUserState(userId: String): List<UserMovieStateEntity>

    @Query("""
        UPDATE user_movie_state
        SET pendingSync = 0, remoteUpdatedAt = :remoteUpdatedAt
        WHERE userId = :userId AND movieId = :movieId
    """)
    suspend fun markSynced(userId: String, movieId: Int, remoteUpdatedAt: Long)

    @Query("""
        UPDATE user_movie_state
        SET isFavorite = :favorite,
            isInWatchlist = :watchlist,
            userRating = :rating,
            pendingSync = 0,
            remoteUpdatedAt = :remoteUpdatedAt
        WHERE userId = :userId AND movieId = :movieId
    """)
    suspend fun applyRemoteState(
        userId: String,
        movieId: Int,
        favorite: Boolean,
        watchlist: Boolean,
        rating: Int,
        remoteUpdatedAt: Long
    )

    @Query("DELETE FROM user_movie_state WHERE userId = :userId")
    suspend fun clearUserState(userId: String)

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getCatalogCount(): Int
}
