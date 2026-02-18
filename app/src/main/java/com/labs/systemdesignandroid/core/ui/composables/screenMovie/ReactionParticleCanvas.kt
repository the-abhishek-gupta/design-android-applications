package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.labs.systemdesignandroid.data.CanvasParticle
import com.labs.systemdesignandroid.domain.MovieReaction
import kotlin.math.pow
import kotlin.random.Random


private const val GRAVITY = 1500f
private const val DRAG = 0.98f
private const val SPRING_STIFFNESS = 400f
private const val SPRING_DAMPING = 0.85f
private const val MAX_PARTICLES = 50  // 🔥 optimized for low-end
private fun easeOutCubic(t: Float): Float {
    val p = t - 1f
    return p * p * p + 1f
}

private fun easeOutExpo(t: Float): Float {
    return if (t == 1f) 1f else 1f - 2f.pow(-10f * t)
}


@Composable
fun ReactionParticleCanvas(
    reaction: MovieReaction, onFinished: () -> Unit
) {
    val density = LocalDensity.current
    val particles = remember { mutableListOf<CanvasParticle>() }

    val textMeasurer = rememberTextMeasurer()

    var isRunning by remember { mutableStateOf(true) }

    // Generate once
    LaunchedEffect(reaction) {
        particles.clear()

        repeat(MAX_PARTICLES) {
            particles += CanvasParticle(
                emoji = reaction.emoji,
                x = 0f,
                y = 0f,
                vx = Random.nextFloat() * 600f - 300f,
                vy = -Random.nextFloat() * 900f - 600f,
                rotation = Random.nextFloat() * 360f,
                angularVelocity = Random.nextFloat() * 180f - 90f,
                scale = Random.nextFloat() * 0.6f + 0.7f,
                alpha = 1f,
                life = 1f
            )
        }
    }

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        if (!isRunning) return@Canvas

        val frameTime = 1f / 60f

        val centerX = size.width / 2
        val centerY = size.height / 2

        particles.forEach { p ->

            // 🎯 Apply spring fling effect outward
            val dx = p.x - centerX
            val dy = p.y - centerY

            val springForceX = -SPRING_STIFFNESS * dx
            val springForceY = -SPRING_STIFFNESS * dy

            p.vx += springForceX * frameTime
            p.vy += springForceY * frameTime

            // Gravity
            p.vy += GRAVITY * frameTime

            // Drag
            p.vx *= DRAG
            p.vy *= DRAG

            // Update position
            p.x += p.vx * frameTime
            p.y += p.vy * frameTime

            // Rotation
            p.rotation += p.angularVelocity * frameTime

            // Fade using cinematic easing
            p.life -= frameTime * 0.7f
//            val eased = easeOutCubic(p.life.coerceIn(0f, 1f))
            val eased = easeOutExpo(p.life)

            p.alpha = eased

            // Draw emoji
            val textLayout = textMeasurer.measure(
                AnnotatedString(p.emoji),
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White.copy(alpha = p.alpha)
                )
            )

            withTransform({
                translate(p.x, p.y)
                rotate(p.rotation)
                scale(p.scale, p.scale)
            }) {
                drawText(textLayout)
            }

        }

        // Stop when all dead
        if (particles.all { it.life <= 0f }) {
            isRunning = false
            onFinished()
        }
    }
}
