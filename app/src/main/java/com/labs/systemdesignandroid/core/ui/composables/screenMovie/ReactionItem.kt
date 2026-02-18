package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ReactionItem(
    reaction: MovieReaction,
    index: Int,
    onReactionClick: (MovieReaction) -> Unit,
    TAG: String = "Abhi.RItem"
) {
    val scale = remember { Animatable(1f) }
    val alpha = remember { Animatable(0f) }
    val translateY = remember { Animatable(20f) }

    LaunchedEffect(Unit) {
        delay(index * 100L)

        scale.animateTo(
            targetValue = 1.4f, animationSpec = tween(durationMillis = 500)
        )
        launch {
            alpha.animateTo(1f, animationSpec = tween(400))
        }
        scale.animateTo(
            targetValue = 1f, animationSpec = tween(durationMillis = 500)
        )
    }
    Text(
        text = reaction.emoji, fontSize = 28.sp, modifier = Modifier
            .graphicsLayer(
                scaleX = scale.value, scaleY = scale.value
            )
            .clickable(onClick = {
                Log.d(TAG, "react: $reaction")
                onReactionClick(reaction)
            })
    )
}
