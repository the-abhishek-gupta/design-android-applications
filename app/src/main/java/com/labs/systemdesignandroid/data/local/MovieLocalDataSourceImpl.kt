package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val dao: MovieDao
) : MovieLocalDataSource {

    override fun observeMovies(userId: String): Flow<List<MovieModel>> =
        dao.observeMoviesWithState(userId).map { rows -> rows.map { it.toDomain() } }

    override suspend fun upsertCatalog(movies: List<MovieCatalogEntity>) {
        dao.upsertCatalog(movies)
    }

    override suspend fun getUserState(userId: String, movieId: Int): UserMovieStateEntity? =
        dao.getUserState(userId = userId, movieId)

    override suspend fun upsertUserState(state: UserMovieStateEntity) {
        dao.upsertUserState(state)
    }

    override suspend fun getPendingUserState(userId: String): List<UserMovieStateEntity> =
        dao.getPendingUserState(userId = userId)

    override suspend fun markSynced(userId: String, movieId: Int, remoteUpdatedAt: Long) {
        dao.markSynced(userId = userId, movieId, remoteUpdatedAt)
    }

    override suspend fun applyRemoteState(
        userId: String,
        movieId: Int,
        favorite: Boolean,
        watchlist: Boolean,
        rating: Int,
        reactions: Set<MovieReaction>,
        remoteUpdatedAt: Long
    ) {
        val existing = dao.getUserState(userId, movieId)
        dao.upsertUserState(
            UserMovieStateEntity(
                userId = userId,
                movieId = movieId,
                isFavorite = favorite,
                isInWatchlist = watchlist,
                userRating = rating,
                reactions = reactions,
                pendingSync = false,
                remoteUpdatedAt = remoteUpdatedAt
            )
        )
    }
    override suspend fun clearUserState(userId: String) {
        dao.clearUserState(userId = userId)
    }

}
