package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.labs.systemdesignandroid.MovieViewModel
import com.labs.systemdesignandroid.data.MovieUiState
import com.labs.systemdesignandroid.domain.SortOrder
import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlin.String

//@Composable
//fun MovieScreenContent(
//    viewModel: MovieViewModel,
//    state: MovieUiState,
//    onSearchChanged: (String) -> Unit,
//    onSortSelected: (SortOrder) -> Unit,
//    onToggleFavorite: (MovieModel) -> Unit,
//    onToggleWatchlist: (MovieModel) -> Unit,
//) {
//    Scaffold(
//        topBar = {
//            MovieTopBar(
//                searchQuery = state.searchQuery,
//                onSearchChanged = onSearchChanged,
//                onSortSelected = onSortSelected
//            )
//        }
//    ) { padding ->
//        if (state.isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else {
//            MovieList(
//                viewModel = viewModel,
//                state = state,
//                movies = state.movies,
//                modifier = Modifier.padding(padding),
//                onToggleFavorite = onToggleFavorite,
//                onToggleWatchlist = onToggleWatchlist,
//            )
//        }
//    }
//}
