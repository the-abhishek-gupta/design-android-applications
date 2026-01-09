package com.labs.systemdesignandroid.domain

import com.labs.systemdesignandroid.data.MovieRepository
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.deleteMovie(movie)
    }
}
