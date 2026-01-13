package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.MovieModel
import javax.inject.Inject

class FilterMoviesByGenreUseCase @Inject constructor() {

    operator fun invoke(
        movies: List<MovieModel>,
        selectedGenres: Set<String>
    ): List<MovieModel> {
        if (selectedGenres.isEmpty()) return movies

        return movies.filter { movie ->
            movie.genres.any { it in selectedGenres }
        }
    }
}
