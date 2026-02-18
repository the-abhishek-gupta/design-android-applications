package com.labs.systemdesignandroid.core.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.labs.systemdesignandroid.MovieViewModel
import com.labs.systemdesignandroid.PlaceholderScreen
import com.labs.systemdesignandroid.Screen
import com.labs.systemdesignandroid.core.ui.composables.screenMovie.MovieScreen
import com.labs.systemdesignandroid.core.ui.composables.utils.ExpandableStackedColumnTest
import com.labs.systemdesignandroid.feature.authentication.composables.AuthRoute
import com.labs.systemdesignandroid.feature.authentication.composables.ProfileScreen
import com.labs.systemdesignandroid.feature.authentication.coordinator.AuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.di.LocalAuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.repository.GoogleAuthRepository
import kotlinx.coroutines.launch


@Composable
fun MainNavigation(
    viewModel: MovieViewModel,
    authRepo: GoogleAuthRepository,
) {

    val coordinator = LocalAuthCoordinator.current

    val items = listOf(
        Screen.Home, Screen.Movie, Screen.Profile, Screen.Settings
    )
    val pagerState = rememberPagerState(pageCount = { items.size })
    val scope = rememberCoroutineScope()


    // Controls whether the auth UI is visible
    var showAuth by remember { mutableStateOf(false) }
    // Listen for sign-in requests from anywhere
    LaunchedEffect(Unit) {
        coordinator.events.collect { e ->
            when (e) {
                is AuthCoordinator.Event.NavigateToAuth -> showAuth = true
                is AuthCoordinator.Event.SignedIn -> Unit
                else -> {

                }
            }
        }
    }
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
                        })
                }
            }
        }) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(innerPadding),
            beyondViewportPageCount = 1
        ) { page ->
            when (items[page]) {
                Screen.Home -> ExpandableStackedColumnTest()
                Screen.Movie -> MovieScreen(
                    viewModel = viewModel,
                    isCurrentPage = pagerState.currentPage == page,
                    onRequireAuth = { afterSignIn ->
                        // page-based "return"
                        val currentRoute = "pager:${pagerState.currentPage}"
                        coordinator.requireSignIn(
                            currentRoute = currentRoute, afterSignIn = afterSignIn
                        )
                    })

                Screen.Settings -> PlaceholderScreen("Settings Screen")
                Screen.Profile -> ProfileScreen(authRepo)
            }
        }
        if (showAuth) {
            androidx.compose.ui.window.Dialog(
                onDismissRequest = {
                    showAuth = false
                    coordinator.clear() // drop pending action if user cancels
                }) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge, tonalElevation = 6.dp
                ) {
                    AuthRoute(
                        authRepo = authRepo,
                        returnRoute = "pager:${pagerState.currentPage}",
                        onDone = {
                            showAuth = false
                        })
                }
            }
        }
    }
}