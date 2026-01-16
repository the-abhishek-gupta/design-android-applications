package com.labs.systemdesignandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.labs.systemdesignandroid.core.ui.theme.SystemDesignAndroidTheme
import com.labs.systemdesignandroid.feature.authentication.composables.AppRoot
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Movie : Screen("movie", "Movie", Icons.Default.Menu)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Profile : Screen("profile", "Profile", Icons.Outlined.AccountCircle)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SystemDesignAndroidTheme {
                val viewModel: MovieViewModel = viewModel()
                AppRoot(serverClientId = BuildConfig.WEB_CLIENT_ID, viewModel = viewModel)
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
