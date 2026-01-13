package com.labs.systemdesignandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.labs.systemdesignandroid.core.ui.composables.MovieDetailOverlay
import com.labs.systemdesignandroid.core.ui.composables.MovieList
import com.labs.systemdesignandroid.core.ui.theme.SystemDesignAndroidTheme
import com.labs.systemdesignandroid.domain.model.Movie
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Movie : Screen("movie", "Movie", Icons.Default.Menu)
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
                Screen.Movie -> MovieTabScreen(viewModel, isCurrentPage = pagerState.currentPage == page)
                Screen.Settings -> PlaceholderScreen("Settings Screen")
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieTabScreen(viewModel: MovieViewModel, isCurrentPage: Boolean) {
    val innerPagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    
    val allMovies by viewModel.movies.collectAsStateWithLifecycle()
    val watchlistMovies by viewModel.watchlistMovies.collectAsStateWithLifecycle()
    val favoriteMovies by viewModel.favoriteMovies.collectAsStateWithLifecycle()
    
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val genres by viewModel.allGenres.collectAsStateWithLifecycle()
    val selectedGenres by viewModel.selectedGenres.collectAsStateWithLifecycle()
    
    var selectedMovieId by remember { mutableStateOf<Int?>(null) }

    // Sync ViewModel filter with inner pager state
    LaunchedEffect(innerPagerState.currentPage) {
        val filter = when (innerPagerState.currentPage) {
            0 -> MovieFilter.ALL
            1 -> MovieFilter.WATCHLIST
            else -> MovieFilter.FAVORITE
        }
        viewModel.onFilterToggled(filter)
    }

    // Reset selection when navigating away
    LaunchedEffect(isCurrentPage) {
        if (!isCurrentPage) {
            selectedMovieId = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                MovieFilterToggle(
                    currentIndex = innerPagerState.currentPage,
                    onFilterSelected = { index ->
                        scope.launch {
                            innerPagerState.animateScrollToPage(index)
                        }
                    }
                )

                HorizontalDivider()

                HorizontalPager(
                    state = innerPagerState,
                    modifier = Modifier.fillMaxSize(),
                    beyondViewportPageCount = 1
                ) { page ->
                    val currentMovies = when (page) {
                        0 -> allMovies
                        1 -> watchlistMovies
                        else -> favoriteMovies
                    }

                    MovieList(
                        movies = currentMovies,
                        genres = if (page == 0) genres else emptyList(),
                        selectedGenres = selectedGenres,
                        onToggleGenre = viewModel::onGenreToggled,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                        onMovieClick = { selectedMovieId = it.id },
                        onToggleFavorite = { movie -> viewModel.toggleFavorite(movie) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        AnimatedContent(
            targetState = selectedMovieId,
            label = "detail",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { id ->
            if (id != null) {
                // Find movie in all lists to ensure we have it
                val movie = (allMovies + watchlistMovies + favoriteMovies).find { it.id == id }
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


@Composable
fun MovieFilterToggle(
    currentIndex: Int,
    onFilterSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(24.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        val filterLabels = listOf("All", "Watchlist", "Favorite")
        filterLabels.forEachIndexed { index, label ->
            val isSelected = currentIndex == index
            val backgroundColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary 
                else Color.Transparent, label = "filter_bg"
            )
            val textColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.onPrimary 
                else MaterialTheme.colorScheme.onSurfaceVariant, label = "filter_text"
            )
            
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor)
                    .clickable { onFilterSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
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
