package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.labs.systemdesignandroid.domain.model.MovieModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieDetailPagerOverlay(
    movies: List<MovieModel>,
    initialMovieId: Int,
    onToggleFavorite: (MovieModel) -> Unit,
    onToggleWatchlist: (MovieModel) -> Unit,
    onUserRatingChanged: (movieId: Int, rating: Int) -> Unit,
    onDismiss: () -> Unit
) {
    if (movies.isEmpty()) return

    val initialIndex = remember(movies, initialMovieId) {
        movies.indexOfFirst { it.id == initialMovieId }.coerceAtLeast(0)
    }

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { movies.size }
    )

    // Optional: if the current movie disappears due to filter change, dismiss safely
    LaunchedEffect(movies) {
        if (movies.none { it.id == initialMovieId }) {
            // best effort: keep open if possible; otherwise dismiss
            // onDismiss()
        }
    }

    // Your existing vertical drag-to-dismiss can wrap the pager
    // (so dragging anywhere dismisses, swiping left/right changes pages)
    MovieDetailOverlayContainer(onDismiss = onDismiss) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val movie = movies[page]

            MovieDetailsOverlay(
                movie = movie,
                onToggleFavorite = onToggleFavorite,
                onToggleWatchlist = onToggleWatchlist,
                onRate = onUserRatingChanged,
                onDismiss = onDismiss
            )
        }
    }
}
