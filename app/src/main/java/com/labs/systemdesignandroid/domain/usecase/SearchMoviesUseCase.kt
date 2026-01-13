package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.model.MovieModel
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor() {

    operator fun invoke(
        movies: List<MovieModel>,
        query: String
    ): List<MovieModel> {
        if (query.isBlank()) return movies

        return movies.filter {
            it.name.contains(query, true)
//                    || it.description.contains(query, true)
//                    || it.genres.any { g -> g.contains(query, true)

        }
    }
}
