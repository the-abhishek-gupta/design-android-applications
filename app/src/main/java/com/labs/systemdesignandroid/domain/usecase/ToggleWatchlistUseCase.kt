package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.Movie
import com.labs.systemdesignandroid.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleWatchlistUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.updateMovie(movie.copy(isInWatchlist = !movie.isInWatchlist))
    }
}
