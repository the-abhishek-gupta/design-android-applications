package com.labs.systemdesignandroid.core.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.labs.systemdesignandroid.feature.authentication.composables.AuthRoute
import com.labs.systemdesignandroid.feature.authentication.coordinator.AuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.repository.GoogleAuthRepository
import com.labs.systemdesignandroid.feature.authentication.routes.Routes
import java.net.URLDecoder

@Composable
fun AppNavGraph(
    authRepo: GoogleAuthRepository,
    coordinator: AuthCoordinator
) {
    val navController = rememberNavController()

    // Listen for auth events from anywhere
    LaunchedEffect(Unit) {
        coordinator.events.collect { e ->
            when (e) {
                is AuthCoordinator.Event.NavigateToAuth -> {
                    val encoded = java.net.URLEncoder.encode(e.returnRoute, "UTF-8")
                    navController.navigate("${Routes.AUTH}?return=$encoded")
                }

                is AuthCoordinator.Event.SignedIn -> {
                    // Do nothing here; AuthRoute will handle returning (more reliable).
                }

                else -> {}
            }
        }
    }

    NavHost(navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                currentRoute = Routes.HOME,
                navController = navController
            )
        }
//        composable(
//            route = "${Routes.MOVIE_DETAIL}/{id}",
//            arguments = listOf(navArgument("id") { type = NavType.LongType })
//        )
//        { backStackEntry ->
//            val id = backStackEntry.arguments?.getLong("id") ?: return@composable
//            MovieDetailsScreen(
//                movieId = id,
//                navController = navController
//            )
//        }
//
//        composable(Routes.WATCHLIST) {
//            WatchlistScreen(navController = navController)
//        }
//
//        composable(Routes.SETTINGS) {
//            SettingsScreen(navController = navController)
//        }
//
        // Auth screen route (returns back)
        composable(
            route = "${Routes.AUTH}?return={return}",
            arguments = listOf(navArgument("return") {
                type = NavType.StringType
                defaultValue = Routes.MOVIES
            })
        )
        { entry ->
            val encoded = entry.arguments?.getString("return") ?: Routes.MOVIES
            val returnRoute = URLDecoder.decode(encoded, "UTF-8")

            AuthRoute(
                authRepo = authRepo,
                returnRoute = returnRoute,
                onDone = {
                    navController.popBackStack() // remove auth from stack
                    // if needed, navigate to returnRoute
                    if (navController.currentBackStackEntry?.destination?.route != returnRoute) {
                        navController.navigate(returnRoute) { launchSingleTop = true }
                    }
                }
            )
        }
    }
}
