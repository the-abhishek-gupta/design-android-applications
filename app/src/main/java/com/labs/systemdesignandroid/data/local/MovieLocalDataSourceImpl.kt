package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val dao: MovieDao
) : MovieLocalDataSource {

    override fun observeMovies(): Flow<List<MovieModel>> =
        dao.observeMoviesWithState().map { rows -> rows.map { it.toDomain() } }

    override suspend fun upsertCatalog(movies: List<MovieCatalogEntity>) {
        dao.upsertCatalog(movies)
    }

    override suspend fun getUserState(movieId: Int): UserMovieStateEntity? =
        dao.getUserState(movieId)

    override suspend fun upsertUserState(state: UserMovieStateEntity) {
        dao.upsertUserState(state)
    }

    override suspend fun getPendingUserState(): List<UserMovieStateEntity> =
        dao.getPendingUserState()

    override suspend fun markSynced(movieId: Int, remoteUpdatedAt: Long) {
        dao.markSynced(movieId, remoteUpdatedAt)
    }

    override suspend fun applyRemoteState(
        movieId: Int,
        favorite: Boolean,
        watchlist: Boolean,
        rating: Int,
        remoteUpdatedAt: Long
    ) {
        dao.applyRemoteState(movieId, favorite, watchlist, rating, remoteUpdatedAt)
    }
    override suspend fun clearUserState() {
        dao.clearUserState()
    }

}
