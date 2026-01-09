package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.Movie
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class UpdateMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.updateMovie(movie)
    }
}
