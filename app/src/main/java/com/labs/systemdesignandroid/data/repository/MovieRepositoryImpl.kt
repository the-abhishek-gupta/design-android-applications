package com.labs.systemdesignandroid.data.repository

import com.labs.systemdesignandroid.data.local.MovieLocalDataSource
import com.labs.systemdesignandroid.data.remote.MovieRemoteDataSource
import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val local: MovieLocalDataSource,
    private val remote: MovieRemoteDataSource
) : MovieRepository {

    override fun observeMovies(): Flow<List<MovieModel>> =
        local.observeMovies()

    override suspend fun refresh() {
        val remoteMovies = remote.fetchMovies()
        local.upsert(remoteMovies)
    }

    override suspend fun toggleFavorite(movieId: Int) {
        val movie = local.getMovieById(movieId) ?: return
        local.upsert(listOf(movie.copy(isFavorite = !movie.isFavorite)))
    }

    override suspend fun toggleWatchlist(movieId: Int) {
        val movie = local.getMovieById(movieId) ?: return
        local.upsert(listOf(movie.copy(isInWatchlist = !movie.isInWatchlist)))
    }

    override suspend fun rateMovie(movieId: Int, rating: Int) {
        val movie = local.getMovieById(movieId) ?: return
        local.upsert(listOf(movie.copy(userRating = rating)))
    }
}
