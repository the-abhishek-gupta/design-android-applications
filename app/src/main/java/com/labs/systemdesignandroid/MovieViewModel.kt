package com.labs.systemdesignandroid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labs.systemdesignandroid.data.MovieUiState
import com.labs.systemdesignandroid.domain.SortOrder
import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.domain.usecase.FilterMoviesByGenreUseCase
import com.labs.systemdesignandroid.domain.usecase.GetAllGenresUseCase
import com.labs.systemdesignandroid.domain.usecase.ObserveMoviesUseCase
import com.labs.systemdesignandroid.domain.usecase.RateMovieUseCase
import com.labs.systemdesignandroid.domain.usecase.SearchMoviesUseCase
import com.labs.systemdesignandroid.domain.usecase.SortMoviesUseCase
import com.labs.systemdesignandroid.domain.usecase.ToggleFavoriteUseCase
import com.labs.systemdesignandroid.domain.usecase.ToggleWatchlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class MovieFilter {
    ALL, WATCHLIST, FAVORITE
}

@HiltViewModel
class MovieViewModel @Inject constructor(
    observeMovies: ObserveMoviesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val toggleWatchlist: ToggleWatchlistUseCase,
    private val searchMovies: SearchMoviesUseCase,
    private val sortMovies: SortMoviesUseCase,
    private val filterByGenre: FilterMoviesByGenreUseCase,
    private val getAllGenres: GetAllGenresUseCase,
    private val rateMovie: RateMovieUseCase,
) : ViewModel() {

    private val TAG = "abhi.MovieViewModel"

    private val _searchQuery = MutableStateFlow("")
    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    private val _selectedGenres = MutableStateFlow<Set<String>>(emptySet())
    private val _filter = MutableStateFlow(MovieFilter.ALL)

    private val moviesFlow = observeMovies()

    val uiState: StateFlow<MovieUiState> =
        combine(
            moviesFlow,
            _filter,
            _searchQuery,
            _sortOrder,
            _selectedGenres
        ) { movies, filter, query, sort, selectedGenres ->
            val filteredByTab = when (filter) {
                MovieFilter.ALL -> movies
                MovieFilter.FAVORITE -> movies.filter { it.isFavorite }
                MovieFilter.WATCHLIST -> movies.filter { it.isInWatchlist }
            }
            val searched = searchMovies(filteredByTab, query)
            val genreFiltered = filterByGenre(searched, selectedGenres)
            val sorted = sortMovies(genreFiltered, sort)
            val allGenres = getAllGenres(movies)
            MovieUiState(
                movies = sorted,
                selectedGenres = selectedGenres,
                searchQuery = query,
                sortOrder = sort,
                allGenres = allGenres,
                filter = filter,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MovieUiState(isLoading = true)
            )


    fun onSearchChanged(query: String) {
        Log.d(TAG, "onSearchChanged: $query")
        _searchQuery.value = query
    }

    fun onSortOrderSelected(order: SortOrder) {
        Log.d(TAG, "onSortOrderSelected: ${order.name}")
        _sortOrder.value = order
    }

    fun onToggleFavorite(movie: MovieModel) {
        Log.d(TAG, "onToggleFavorite: $movie")
        viewModelScope.launch {
            toggleFavorite(movie.id)
        }
    }

    fun onToggleWatchlist(movie: MovieModel) {
        Log.d(TAG, "onToggleWatchlist: $movie")
        viewModelScope.launch {
            toggleWatchlist(movie.id)
        }
    }

    fun onGenreToggled(genre: String) {
        Log.d(TAG, "onGenreToggled: $genre")
        _selectedGenres.update { current ->
            if (genre in current) current - genre else current + genre
        }
    }
    fun onFilterChanged(filter: MovieFilter) {
        Log.d(TAG, "onFilterChanged: ${filter.name}")
        _filter.value = filter
    }

    fun onRateMovie(movieId: Int, rating: Int) {
        Log.d(TAG, "onRateMovie: ${movieId} ${rating} stars")
        viewModelScope.launch {
            rateMovie(movieId, rating)
        }
    }
}
