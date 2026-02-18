package com.labs.systemdesignandroid.core.ui.composables.screenMovie


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.labs.systemdesignandroid.data.EmojiParticle
import com.labs.systemdesignandroid.utils.random
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


@Composable
fun ChaoticEmojiParticle(
    particle: EmojiParticle, onAnimationComplete: () -> Unit = {}
) {
    val density = LocalDensity.current

    val animY = remember { Animatable(-100f) }
    val animX = remember { Animatable(particle.x) }
    val animAlpha = remember { Animatable(1f) }
    val animRotation = remember { Animatable(particle.rotation) }
    val animScale = remember { Animatable(particle.scale) }

    // Swing/wobble offset
    val swingOffset = remember { Animatable(0f) }

//    LaunchedEffect(particle.reaction) {
//        // Calculate duration based on velocity
//        val duration = ((particle.targetY - (-100f)) / particle.velocityY * 1000).toInt()
//
//        // Launch all animations in parallel
//        coroutineScope {
//            // Vertical fall with easing
//            launch {
//                animY.animateTo(
//                    particle.targetY, tween(
//                        durationMillis = duration, easing = FastOutSlowInEasing
//                    )
//                )
//            }
//
//            // Horizontal drift with chaotic movement
//            launch {
//                animX.animateTo(
//                    particle.targetX, tween(
//                        durationMillis = duration, easing = LinearEasing
//                    )
//                )
//            }
//
//            // Continuous swing/wobble (cancels when parent scope ends)
//            launch {
//                try {
//                    while (true) {
//                        swingOffset.animateTo(
//                            particle.swingAmplitude, tween(
//                                durationMillis = (500 / particle.swingFrequency).toInt(),
//                                easing = LinearEasing
//                            )
//                        )
//                        swingOffset.animateTo(
//                            -particle.swingAmplitude, tween(
//                                durationMillis = (500 / particle.swingFrequency).toInt(),
//                                easing = LinearEasing
//                            )
//                        )
//                    }
//                } catch (e: Exception) {
//                    // Animation cancelled, that's okay
//                }
//            }
//
//            // Continuous rotation
//            launch {
//                animRotation.animateTo(
//                    particle.rotation + (360f..720f).random() * if (Random.nextBoolean()) 1 else -1,
//                    tween(
//                        durationMillis = duration, easing = LinearEasing
//                    )
//                )
//            }
//
//            // Scale variation during fall
//            launch {
//                animScale.animateTo(
//                    particle.scale * (0.8f..1.2f).random(), tween(
//                        durationMillis = duration / 2, easing = LinearEasing
//                    )
//                )
//                animScale.animateTo(
//                    particle.scale * (0.6f..1f).random(), tween(
//                        durationMillis = duration / 2, easing = LinearEasing
//                    )
//                )
//            }
//
//            // Fade out in the last portion
//            launch {
//                delay((duration * 0.6).toLong())
//                animAlpha.animateTo(0f, tween((duration * 0.4).toInt()))
//            }
//        }
//
//        // All animations complete - notify parent
//        onAnimationComplete()
//    }

    Text(
        text = particle.reaction.emoji,
        fontSize = 20.sp,
        modifier = Modifier
            .offset(
                x = with(density) { (animX.value + swingOffset.value).toDp() },
                y = with(density) { animY.value.toDp() })
            .graphicsLayer {
                rotationZ = animRotation.value
                alpha = animAlpha.value
                scaleX = animScale.value
                scaleY = animScale.value
            })
}