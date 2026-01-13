package com.labs.systemdesignandroid.data

import com.labs.systemdesignandroid.MovieFilter
import com.labs.systemdesignandroid.domain.SortOrder
import com.labs.systemdesignandroid.domain.model.MovieModel

data class MovieUiState(
    val movies: List<MovieModel> = emptyList(),
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NONE,
    val allGenres: List<String> = emptyList(),
    val selectedGenres: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val filter: MovieFilter = MovieFilter.ALL,
)
