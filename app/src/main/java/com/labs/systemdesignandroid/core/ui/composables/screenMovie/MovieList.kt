package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.labs.systemdesignandroid.domain.model.MovieModel


@Composable
fun MovieList(
    movies: List<MovieModel>,
    onToggleFavorite: (MovieModel) -> Unit,
    onToggleWatchlist: (MovieModel) -> Unit,
    onMovieClick: (MovieModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = movies,
            key = { it.id }
        ) { movie ->
            MovieItem(
                movie = movie,
                onToggleFavorite = onToggleFavorite,
                onToggleWatchlist = onToggleWatchlist,
                modifier = Modifier.clickable { onMovieClick(movie) }
            )
            HorizontalDivider()
        }
    }
}
