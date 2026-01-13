package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.labs.systemdesignandroid.domain.model.MovieModel
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsOverlay(
    movie: MovieModel,
    onToggleFavorite: (MovieModel) -> Unit,
    onToggleWatchlist: (MovieModel) -> Unit,
    onDismiss: () -> Unit
) {
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val dismissThresholdPx = 280f
    val scrollState = rememberScrollState()

    // Allow drag-to-dismiss only when description is at top
    val dragEnabled = scrollState.value == 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .graphicsLayer {
                alpha = 1f - (kotlin.math.abs(offsetY.value) / 1200f).coerceIn(0f, 0.15f)
            }
            .draggable(
                enabled = dragEnabled,
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    scope.launch {
                        offsetY.snapTo(
                            (offsetY.value + delta * 0.6f).coerceIn(-600f, 600f)
                        )
                    }
                },
                onDragStopped = { velocity ->
                    scope.launch {
                        if (kotlin.math.abs(offsetY.value) > dismissThresholdPx ||
                            kotlin.math.abs(velocity) > 1500f
                        ) {
                            onDismiss()
                        } else {
                            offsetY.animateTo(0f)
                        }
                    }
                }
            )
    ) {
        // Background image
        AsyncImage(
            model = movie.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // better than FillBounds for posters
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.4f to Color.Transparent,
                        0.7f to Color.Black.copy(alpha = 0.8f),
                        1f to Color.Black
                    )
                )
        )

        // Favorite Button
        OutlinedButton(
            onClick = { onToggleFavorite(movie) },
            modifier = Modifier
                .padding(end = 12.dp, top = 12.dp)
                .align(Alignment.TopEnd),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (movie.isFavorite) "Remove from" else "Mark as", fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if (movie.isFavorite) Color.Red else LocalContentColor.current
            )
        }

        // Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movie.name,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = movie.rating.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text("${movie.year}", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.8f))
                Text("${movie.durationMinutes} min", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.8f))
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = movie.genres.joinToString(" • "),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(20.dp))

            var expanded by rememberSaveable(movie.id) { mutableStateOf(false) }
            val desc = movie.description

            Text(
                text = "$desc $desc $desc $desc $desc ",
                maxLines = if (expanded) Int.MAX_VALUE else 4,
                overflow = if (!expanded) TextOverflow.Ellipsis else TextOverflow.Visible,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 26.sp,
                modifier = Modifier
                    .height(120.dp)
                    .verticalScroll(scrollState)
                    .clickable { expanded = !expanded }
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (movie.isInWatchlist) {
                    Button(
                        onClick = { onToggleWatchlist(movie) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("In Watchlist", fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(
                        onClick = { onToggleWatchlist(movie) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Watchlist", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
