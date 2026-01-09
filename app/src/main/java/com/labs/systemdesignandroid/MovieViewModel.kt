package com.labs.systemdesignandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labs.systemdesignandroid.domain.AddMovieUseCase
import com.labs.systemdesignandroid.domain.DeleteMovieUseCase
import com.labs.systemdesignandroid.domain.GetMoviesUseCase
import com.labs.systemdesignandroid.domain.Movie
import com.labs.systemdesignandroid.domain.SortOrder
import com.labs.systemdesignandroid.domain.UpdateMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addMovieUseCase: AddMovieUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase
) : ViewModel() {

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val movies: StateFlow<List<Movie>> = combine(
        getMoviesUseCase(),
        _sortOrder,
        _searchQuery
    ) { movies, sortOrder, query ->
        val filtered = if (query.isBlank()) {
            movies
        } else {
            movies.filter { movie ->
                movie.name.contains(query, ignoreCase = true) ||
                        movie.genres.any { it.contains(query, ignoreCase = true) }
            }
        }

        when (sortOrder) {
            SortOrder.NONE -> filtered
            SortOrder.NAME_ASC -> filtered.sortedBy { it.name }
            SortOrder.NAME_DESC -> filtered.sortedByDescending { it.name }
            SortOrder.GRADE_ASC -> filtered.sortedBy { it.genres.firstOrNull() ?: "" }
            SortOrder.GRADE_DESC -> filtered.sortedByDescending { it.genres.firstOrNull() ?: "" }
        }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSortOrderSelected(order: SortOrder) {
        _sortOrder.value = order
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun addMovie(name: String, genre: String) {
        viewModelScope.launch {
            val newMovie = Movie(
                id = Random.nextInt(),
                name = name,
                genres = listOf(genre),
                durationMinutes = 120,
                rating = 0.0,
                year = 2024,
                imageUrl = "https://picsum.photos/300/450?random=${Random.nextInt()}",
                description = "New movie description"
            )
            addMovieUseCase(newMovie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            deleteMovieUseCase(movie)
        }
    }

    fun getMovieById(id: Int): StateFlow<Movie?> {
        return getMoviesUseCase()
            .map { movies -> movies.find { it.id == id } }
            .flowOn(Dispatchers.Default)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            updateMovieUseCase(movie)
        }
    }
}
