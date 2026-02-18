package com.labs.systemdesignandroid.core.ui.composables.screenMovie


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.coroutines.launch


@Composable
fun RainEmoji(
    trigger: MovieReaction?, onAnimationFinished: () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(0f) }

    // Using 'trigger' as the key means this block runs
    // every time reactionTrigger changes in the parent
    LaunchedEffect(trigger) {
        trigger?.let {
            launch { alpha.snapTo(0f) }
            scale.snapTo(1f)

            // 2. Play animation sequence
            launch { alpha.animateTo(1f, tween(200)) }
            scale.animateTo(2f, tween(300))
            scale.animateTo(1f, tween(300))
//            alpha.animateTo(0f, tween(200))
//            scale.snapTo(1f)
//            alpha.snapTo(0f)
//            launch { alpha.animateTo(1f, tween(300)) }
//
//            scale.animateTo(2f, tween(300))
//            scale.animateTo(1f, tween(300))
            launch {
                alpha.animateTo(0f, tween(300))
                onAnimationFinished()
            }
        }
    }
    if (trigger != null) {
        Text(
            text = trigger.emoji, modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            })
    }
}