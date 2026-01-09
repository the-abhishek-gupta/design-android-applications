package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.Movie
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.deleteMovie(movie)
    }
}
