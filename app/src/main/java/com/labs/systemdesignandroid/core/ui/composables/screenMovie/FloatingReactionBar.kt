package com.labs.systemdesignandroid.core.ui.composables.screenMovie


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.labs.systemdesignandroid.data.EmojiParticle
import com.labs.systemdesignandroid.domain.MovieReaction
import com.labs.systemdesignandroid.utils.random
import kotlinx.coroutines.delay


@Composable
fun FloatingReactionBar(
    onReact: (MovieReaction) -> Unit, onDismiss: () -> Unit
) {
    var particles by remember { mutableStateOf<List<EmojiParticle>>(emptyList()) }
    var isDismissing by remember { mutableStateOf(false) }
    var animateItems by remember { mutableStateOf(false) }
    var particleAnimationsComplete by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        animateItems = true
    }

    // Auto-dismiss when all particles complete
    LaunchedEffect(particleAnimationsComplete) {
        if (particleAnimationsComplete > 0 && particleAnimationsComplete >= particles.size) {
            isDismissing = true
            delay(300) // Wait for dismiss animation
            onReact(particles.firstOrNull()?.reaction ?: return@LaunchedEffect)
        }
    }

    Popup(
        onDismissRequest = onDismiss, properties = PopupProperties(focusable = true)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                }) {

            val screenWidth = constraints.maxWidth.toFloat()
            val screenHeight = constraints.maxHeight.toFloat()

            // Particle system - use key for proper recomposition
            particles.forEach { particle ->
                key(particle.reaction.hashCode() + particle.particleIndex) {
                    ChaoticEmojiParticle(
                        particle = particle,
                        onAnimationComplete = {
                            particleAnimationsComplete++
                        }
                    )
                }
            }

            // Reaction bar
            AnimatedVisibility(
                visible = !isDismissing,
                exit = fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300)),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    shadowElevation = 10.dp,
                    color = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures { /* Consume tap */ }
                    }) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MovieReaction.entries.forEachIndexed { index, reaction ->
                            ReactionButton(
                                reaction = reaction,
                                index = index,
                                animateItems = animateItems,
                                onReactionClick = { clickedReaction ->
                                    // Reset particle completion counter
                                    particleAnimationsComplete = 0

                                    // Generate particles
                                    particles = List(50) { i ->
                                        val startX =
                                            (screenWidth * 0.05f..screenWidth * 0.95f).random()

                                        EmojiParticle(
                                            reaction = clickedReaction,
                                            particleIndex = i,
                                            x = startX,
                                            targetX = startX + (-300f..300f).random(),
                                            targetY = screenHeight + 100f,
                                            rotation = (0..360).random().toFloat(),
                                            velocityX = (-100f..100f).random(),
                                            velocityY = (300f..800f).random(),
                                            swingAmplitude = (20f..60f).random(),
                                            swingFrequency = (1f..3f).random(),
                                            scale = (0.5f..1.5f).random()
                                        )
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}
