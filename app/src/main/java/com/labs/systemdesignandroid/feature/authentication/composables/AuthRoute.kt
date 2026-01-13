package com.labs.systemdesignandroid.feature.authentication.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.labs.systemdesignandroid.feature.authentication.di.LocalAuthCoordinator
import com.labs.systemdesignandroid.feature.authentication.repository.GoogleAuthRepository
import kotlinx.coroutines.launch


@Composable
fun AuthRoute(
    authRepo: GoogleAuthRepository, returnRoute: String, onDone: () -> Unit
) {
    val coordinator = LocalAuthCoordinator.current
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Box(
        Modifier
            .fillMaxSize()
            .padding(24.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Sign in required", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            Button(
                enabled = !loading, onClick = {
                    loading = true
                    error = null
                    scope.launch {
                        try {
                            authRepo.signInWithGoogle()
                            coordinator.onSignedIn() // executes pending action once
                            loading = false
                            onDone()
                        } catch (t: Throwable) {
                            loading = false
                            error = t.message ?: "Sign-in failed"
                        }
                    }
                }) {
                if (loading) {
                    CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(12.dp))
                }
                Text("Continue with Google")
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            Text("Returning to: $returnRoute", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun SignInTemplateScreen(
    appName: String = "MovieVault",
    tagline: String = "Track, Review, and Discover",
    welcomeTitle: String = "Welcome to MovieVault",
    welcomeSubtitle: String = "Sign in to start tracking and reviewing your favorite movies",
    googleButtonText: String = "Continue with Google",
    helperText: String = "No account needed – just sign in with Google to get started",
    footerText: String = "By signing in, you agree to our Terms of Service and Privacy Policy",
    appIcon: @Composable () -> Unit,
    googleIcon: @Composable () -> Unit,
    onTermsClick: (() -> Unit)? = null,
    onPrivacyClick: (() -> Unit)? = null,
    isLoading: Boolean = false,
    errorText: String? = null,
    background: (@Composable () -> Unit)? = null,
    onContinueWithGoogle: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {

        // Background layer
        if (background != null) {
            background()
        } else {
            DefaultSignInBackground()
        }

        // Dark overlay vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.25f), Color.Black.copy(alpha = 0.80f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            // App logo circle (purple gradient)
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFFB54CFF), Color(0xFFFF4DA0)
                            )
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                appIcon()
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = appName,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = tagline,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.65f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = welcomeTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = welcomeSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(18.dp))

                    GoogleButton(
                        text = googleButtonText,
                        leadingIcon = googleIcon,
                        enabled = !isLoading,
                        isLoading = isLoading,
                        onClick = onContinueWithGoogle
                    )

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = helperText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.55f),
                        textAlign = TextAlign.Center
                    )

                    errorText?.let {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Footer terms text (with optional click handlers)
            TermsFooter(
                text = footerText, onTermsClick = onTermsClick, onPrivacyClick = onPrivacyClick
            )

            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun DefaultSignInBackground() {
    // Mimics the "cinema" dark background vibe using gradients + blur (no image needed)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF050A16), Color(0xFF030611)
                    )
                )
            )
    )

    // Soft blobs
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(60.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF5C2DFF).copy(alpha = 0.18f), Color.Transparent
                    ), radius = 850f
                )
            )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(70.dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF2F9B).copy(alpha = 0.12f), Color.Transparent
                    ), radius = 900f
                )
            )
    )
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    Surface(
        modifier = modifier,
        shape = shape,
        color = Color(0xFF0B1226).copy(alpha = 0.55f),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        // subtle stroke like your screenshot
        Box(
            modifier = Modifier.border(
                width = 1.dp, color = Color.White.copy(alpha = 0.08f), shape = shape
            )
        ) { content() }
    }
}

@Composable
private fun GoogleButton(
    text: String,
    leadingIcon: @Composable () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .clickable(enabled = enabled && !isLoading, onClick = onClick),
        shape = shape,
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.Black
                    )
                    Spacer(Modifier.width(12.dp))
                } else {
                    leadingIcon()
                    Spacer(Modifier.width(10.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF0B0F1A),
                )
            }
        }
    }
}

@Composable
private fun TermsFooter(
    text: String,
    onTermsClick: (() -> Unit)?,
    onPrivacyClick: (() -> Unit)?,
) {
    // Simple version like screenshot. If you want actual clickable spans, tell me — I’ll provide AnnotatedString version.
    val clickable = (onTermsClick != null || onPrivacyClick != null)

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = Color.White.copy(alpha = 0.40f),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (clickable) Modifier.padding(horizontal = 6.dp) else Modifier
            )
    )
}

