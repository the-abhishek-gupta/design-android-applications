package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labs.systemdesignandroid.MovieViewModel

@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    isCurrentPage : Boolean
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedMovieId by rememberSaveable { mutableStateOf<Int?>(null) }

    // Reset selection when navigating away
    LaunchedEffect(isCurrentPage) {
        if (!isCurrentPage) {
            selectedMovieId = null
        }
    }
    Box(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            MovieTopBar(
                searchQuery = state.searchQuery,
                selectedTab = state.filter,
                onTabSelected = viewModel::onFilterChanged,
                onSortSelected = viewModel::onSortOrderSelected,
                onSearchChanged = viewModel::onSearchChanged,
            )

            GenreChipsFilter(
                genres = state.allGenres,
                selectedGenres = state.selectedGenres,
                onToggle = viewModel::onGenreToggled
            )

            Spacer(modifier = Modifier.height(8.dp))

            MovieList(
                movies = state.movies,
                onMovieClick = { movie -> selectedMovieId = movie.id },
                onToggleFavorite = viewModel::onToggleFavorite,
                onToggleWatchlist = viewModel::onToggleWatchlist
            )
        }

        val selectedMovie = remember(state.movies, selectedMovieId) {
            selectedMovieId?.let { id -> state.movies.firstOrNull { it.id == id } }
        }

        AnimatedVisibility(
            visible = selectedMovie != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            selectedMovie?.let { movie ->
                MovieDetailsOverlay(
                    movie = movie,
                    onToggleFavorite = viewModel::onToggleFavorite,
                    onToggleWatchlist = viewModel::onToggleWatchlist,
                    onDismiss = { selectedMovieId = null }
                )
            }
        }
    }

}
