package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ReactionButton(
    reaction: MovieReaction,
    index: Int,
    animateItems: Boolean,
    onReactionClick: (MovieReaction) -> Unit
) {
    val itemScale = remember { Animatable(0f) }
    val itemAlpha = remember { Animatable(0f) }
    val translateY = remember { Animatable(20f) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    // Entry animation
    LaunchedEffect(animateItems) {
        if (animateItems) {
            delay(index * 60L)
            coroutineScope {
                launch {
                    itemScale.animateTo(
                        1f, spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
                launch {
                    itemAlpha.animateTo(1f, tween(300))
                }
                launch {
                    translateY.animateTo(
                        targetValue = 0f, animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = itemScale.value
                scaleY = itemScale.value
                alpha = itemAlpha.value
                translationY = translateY.value
            }
            .clickable(
                interactionSource = interactionSource, indication = ripple(
                    bounded = false, radius = 24.dp
                ), onClick = {
                    scope.launch {
                            itemScale.animateTo(
                                1.5f, spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessHigh
                                )
                            )
                            itemScale.animateTo(
                                1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )

                        delay(80)
                        onReactionClick(reaction)
                    }
                })
            .padding(4.dp)
    ) {
        Text(
            text = reaction.emoji, fontSize = 28.sp, modifier = Modifier.align(Alignment.Center)
        )
    }
}