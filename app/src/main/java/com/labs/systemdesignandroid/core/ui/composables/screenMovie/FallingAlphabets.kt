package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.labs.systemdesignandroid.data.EmojiParticle
import com.labs.systemdesignandroid.domain.MovieReaction
import com.labs.systemdesignandroid.utils.random
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun RainingEmoji(
    trigger: MovieReaction?,
    onAnimationFinished: () -> Unit
) {
    val textMeasurer = rememberTextMeasurer()

    val particles = remember { mutableListOf<EmojiParticle>() }

    var frameTime by remember { mutableLongStateOf(0L) }
    var screenWidth by remember { mutableFloatStateOf(0f) }
    var screenHeight by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(trigger) {
        if (trigger == null) return@LaunchedEffect

        particles.clear()

        repeat(20) { index ->
            particles.add(
                EmojiParticle(
                    reaction = trigger,
                    baseX = Random.nextFloat() * screenWidth,
                    x = Random.nextFloat() * screenWidth,
                    y = -Random.nextFloat() * 800f,
                    rotation = (0..360).random().toFloat(),
                    velocityX = (-100f..100f).random(),
                    velocityY = (300f..800f).random(),
                    swingAmplitude = (20f..60f).random(),
                    swingFrequency = (1f..3f).random(),
                    scale = (0.5f..1.5f).random(),
                    particleIndex = index
                )
            )
        }

        var lastFrameTime = withFrameNanos { it }

        while (particles.isNotEmpty()) {

            val current = withFrameNanos { it }
            val deltaSeconds = (current - lastFrameTime) / 1_000_000_000f
            lastFrameTime = current

            val iterator = particles.iterator()

            while (iterator.hasNext()) {
                val p = iterator.next()

                val swing =
                    sin(current / 1_000_000_000f * p.swingFrequency) * p.swingAmplitude

//                p.x += (p.velocityX * deltaSeconds) + swing * deltaSeconds
//                p.x = p.baseX + swing
                p.x = p.baseX
                p.y += p.velocityY * deltaSeconds
                p.rotation += 60f * deltaSeconds

                if (p.y > screenHeight + 200f) {
                    iterator.remove()
                }
            }

            frameTime = current // single invalidation trigger
        }

        onAnimationFinished()
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                screenWidth = it.width.toFloat()
                screenHeight = it.height.toFloat()
            }
    ) {
        // read frameTime to trigger redraw
        frameTime

        particles.forEach { particle ->
            rotate(particle.rotation) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = particle.reaction.emoji,
                    topLeft = Offset(particle.x, particle.y),
                    style = TextStyle(
                        fontSize = 24.sp * particle.scale
                    )
                )
            }
        }
    }
}

