package com.labs.systemdesignandroid.domain.usecase

import com.labs.systemdesignandroid.domain.SortOrder
import com.labs.systemdesignandroid.domain.model.MovieModel
import javax.inject.Inject

class SortMoviesUseCase @Inject constructor() {

    operator fun invoke(
        movies: List<MovieModel>,
        order: SortOrder
    ): List<MovieModel> =
        when (order) {
            SortOrder.NONE -> movies
            SortOrder.NAME_ASC -> movies.sortedBy { it.name }
            SortOrder.NAME_DESC -> movies.sortedByDescending { it.name }
            SortOrder.RATING_DESC -> movies.sortedByDescending { it.rating }
        }
}
