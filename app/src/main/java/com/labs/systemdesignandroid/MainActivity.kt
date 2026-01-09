package com.labs.systemdesignandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.labs.systemdesignandroid.core.ui.composables.MovieDetailOverlay
import com.labs.systemdesignandroid.core.ui.composables.MovieList
import com.labs.systemdesignandroid.core.ui.theme.SystemDesignAndroidTheme
import com.labs.systemdesignandroid.domain.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Movie : Screen("movie", "Movie", Icons.Default.Menu)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.Star)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SystemDesignAndroidTheme {
                val viewModel: MovieViewModel = viewModel()
                MainNavigation(viewModel)
            }
        }
    }
}

@Composable
fun MainNavigation(viewModel: MovieViewModel) {
    val items = listOf(
        Screen.Home,
        Screen.Movie,
        Screen.Analytics,
        Screen.Settings
    )
    val pagerState = rememberPagerState(pageCount = { items.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding),
            beyondViewportPageCount = 1
        ) { page ->
            when (items[page]) {
                Screen.Home -> PlaceholderScreen("Home Screen")
                Screen.Movie -> MovieScreen(viewModel, isCurrentPage = pagerState.currentPage == page)
                Screen.Analytics -> PlaceholderScreen("Analytics Screen")
                Screen.Settings -> PlaceholderScreen("Settings Screen")
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieScreen(viewModel: MovieViewModel, isCurrentPage: Boolean) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val genres by viewModel.allGenres.collectAsStateWithLifecycle()
    val selectedGenres by viewModel.selectedGenres.collectAsStateWithLifecycle()
    
    var selectedMovieId by remember { mutableStateOf<Int?>(null) }

    // Reset selection when navigating away
    LaunchedEffect(isCurrentPage) {
        if (!isCurrentPage) {
            selectedMovieId = null
        }
    }

    SharedTransitionLayout {
        AnimatedVisibility(visible = true, label = "root") {
            val listScope = this
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    MovieList(
                        movies = movies,
                        genres = genres,
                        selectedGenres = selectedGenres,
                        onToggleGenre = viewModel::onGenreToggled,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onMovieClick = { selectedMovieId = it.id },
                        onToggleFavorite = { movie -> viewModel.toggleFavorite(movie) },
                        modifier = Modifier.padding(innerPadding),
                        onSortOrderSelected = { viewModel.onSortOrderSelected(it) }
                    )
                }

                AnimatedContent(
                    targetState = selectedMovieId,
                    label = "detail",
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { id ->
                    if (id != null) {
                        val movie = movies.find { it.id == id }
                        if (movie != null) {
                            MovieDetailOverlay(
                                movie = movie,
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onToggleWatchlist = { viewModel.toggleWatchlist(it) },
                                onDismiss = { selectedMovieId = null }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}
