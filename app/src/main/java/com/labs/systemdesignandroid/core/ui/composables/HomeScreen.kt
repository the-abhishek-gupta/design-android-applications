package com.labs.systemdesignandroid.core.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.labs.systemdesignandroid.feature.authentication.di.LocalAuthCoordinator

@Composable
fun HomeScreen(currentRoute: String, navController: NavController) {
    val coordinator = LocalAuthCoordinator.current

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Button(onClick = {
            coordinator.requireSignIn(currentRoute) {
                // This runs AFTER successful login
                navController.navigate("profile")
            }
        }) {
            Text("Go to Profile (requires sign-in)")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            coordinator.requireSignIn(currentRoute) {
                // A protected action (e.g. like/save)
                // call your VM function here
            }
        }) {
            Text("Like (requires sign-in)")
        }
    }
}
