package com.labs.systemdesignandroid.feature.authentication.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import com.labs.systemdesignandroid.feature.authentication.coordinator.AuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.di.LocalAuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.repository.GoogleAuthRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authRepo: GoogleAuthRepository, modifier: Modifier = Modifier
) {
    val coordinator = LocalAuthCoordinator.current

    // Firebase user (simple; if you want reactive updates, wire a flow)
    var user by remember { mutableStateOf(authRepo.currentUser()) }
    var snack by remember { mutableStateOf<String?>(null) }

    // refresh helper (call after sign-in/out)
    fun refreshUser() {
        user = authRepo.currentUser()
    }

    // When auth coordinator succeeds, refresh UI
    LaunchedEffect(Unit) {
        coordinator.events.collect { e ->
            if (e is AuthCoordinator.Event.SignedIn) refreshUser()
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        LargeTopAppBar(
            title = { Text("Profile") })
    }, snackbarHost = {
        SnackbarHost(hostState = remember { SnackbarHostState() })
    }) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            ProfileHeaderCard(user = user, onSignIn = {
                coordinator.requireSignIn(
                    currentRoute = "pager:profile", // since you use pager, any string is fine
                    afterSignIn = {
                        refreshUser()
                        snack = "Signed in"
                    })
            }, onLogout = {
                authRepo.signOut()
                refreshUser()
                snack = "Logged out"
                coordinator.clear()
            })

            Spacer(Modifier.height(16.dp))

            SettingsSection(
                isSignedIn = user != null, onRequireSignIn = {
                    coordinator.requireSignIn("pager:profile") { refreshUser() }
                })
        }
    }

    // Simple snack handling
    LaunchedEffect(snack) {
        snack?.let {
            // If you already have a global SnackbarHostState, plug it in.
            // Keeping it simple here:
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    user: FirebaseUser?, onSignIn: () -> Unit, onLogout: () -> Unit
) {
    // A soft gradient header card
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Avatar(
                        photoUrl = user?.photoUrl?.toString(),
                        fallbackInitial = user?.displayName?.firstOrNull()?.uppercase() ?: "G"
                    )

                    Spacer(Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user?.displayName ?: "Guest",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = user?.email ?: "Sign in to sync watchlist and favorites",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (user == null) {
                    Button(
                        onClick = onSignIn,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.Login, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Text("Continue with Google")
                    }

                    Spacer(Modifier.height(10.dp))

                    AssistChip(
                        onClick = { /* optional */ },
                        label = { Text("Why sign in?") },
                        leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) })
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onLogout,
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                            Spacer(Modifier.width(10.dp))
                            Text("Logout")
                        }

                        Button(
                            onClick = { /* optional: open account settings */ },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Icon(Icons.Outlined.ManageAccounts, contentDescription = null)
                            Spacer(Modifier.width(10.dp))
                            Text("Account")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Avatar(
    photoUrl: String?, fallbackInitial: String
) {
    val size = 56.dp
    Surface(
        modifier = Modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        if (!photoUrl.isNullOrBlank()) {
            // Requires Coil: implementation("io.coil-kt:coil-compose:2.6.0")
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fallbackInitial, style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    isSignedIn: Boolean, onRequireSignIn: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            "Preferences",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(10.dp))

        ElevatedCard(
            shape = MaterialTheme.shapes.extraLarge, modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(8.dp)) {

                ProfileRow(
                    icon = Icons.Outlined.FavoriteBorder,
                    title = "Sync favorites",
                    subtitle = if (isSignedIn) "Enabled" else "Sign in required",
                    onClick = { if (isSignedIn) Unit else onRequireSignIn() },
                    trailing = {
                        Switch(
                            checked = isSignedIn,
                            onCheckedChange = { if (!isSignedIn) onRequireSignIn() })
                    })

                Divider(Modifier.padding(horizontal = 12.dp))

                ProfileRow(
                    icon = Icons.Outlined.BookmarkBorder,
                    title = "Sync watchlist",
                    subtitle = if (isSignedIn) "Enabled" else "Sign in required",
                    onClick = { if (isSignedIn) Unit else onRequireSignIn() })

                Divider(Modifier.padding(horizontal = 12.dp))

                ProfileRow(
                    icon = Icons.Outlined.PrivacyTip,
                    title = "Privacy",
                    subtitle = "Manage data & permissions",
                    onClick = { /* open privacy */ })

                Divider(Modifier.padding(horizontal = 12.dp))

                ProfileRow(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Help",
                    subtitle = "FAQ & support",
                    onClick = { /* open help */ })
            }
        }
    }
}

@Composable
private fun ProfileRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    trailing: @Composable (() -> Unit)? = null
) {
    ListItem(
        leadingContent = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        headlineContent = { Text(title) },
        supportingContent = {
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = trailing,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(horizontal = 6.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
    )
}
