package com.labs.systemdesignandroid.data.local

import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val dao: MovieDao
) : MovieLocalDataSource {

    override fun observeMovies(): Flow<List<MovieModel>> =
        dao.getAllMovies().map { it.map(MovieEntity::toDomain) }

    override suspend fun upsert(movies: List<MovieModel>) {
        dao.insertMovies(movies.map(MovieModel::toEntity))
    }

    override suspend fun getMovieById(id: Int): MovieModel? =
        dao.getMovieById(id)?.toDomain()
}
