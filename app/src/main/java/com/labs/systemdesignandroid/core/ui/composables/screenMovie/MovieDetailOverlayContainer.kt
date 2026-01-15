package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch

@Composable
fun MovieDetailOverlayContainer(
    onDismiss: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val dismissThreshold = 280f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .graphicsLayer {
                alpha = 1f - (kotlin.math.abs(offsetY.value) / 1200f).coerceIn(0f, 0.15f)
            }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    scope.launch {
                        offsetY.snapTo((offsetY.value + delta * 0.6f).coerceIn(-600f, 600f))
                    }
                },
                onDragStopped = { velocity ->
                    scope.launch {
                        if (kotlin.math.abs(offsetY.value) > dismissThreshold ||
                            kotlin.math.abs(velocity) > 1500f
                        ) onDismiss()
                        else offsetY.animateTo(0f)
                    }
                }
            )
    ) {
        content()
    }
}
