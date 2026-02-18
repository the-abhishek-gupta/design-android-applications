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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.labs.systemdesignandroid.core.ui.composables.utils.ExpandableStackedColumn
import com.labs.systemdesignandroid.core.ui.composables.utils.GlitterText
import com.labs.systemdesignandroid.domain.MovieReaction
import com.labs.systemdesignandroid.domain.model.MovieModel
import com.labs.systemdesignandroid.feature.comments.ui.CommentsAndRepliesSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsOverlay(
    movie: MovieModel,
    onRate: (movieId: Int, rating: Int) -> Unit,
    onToggleFavorite: (MovieModel) -> Unit,
    onToggleWatchlist: (MovieModel) -> Unit,
    onDismiss: () -> Unit,
    onReact: (Int, MovieReaction, Boolean) -> Unit
) {
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val dismissThresholdPx = 280f
    val reviewThresholdPx = 0f
    val scrollState = rememberScrollState()
    var reactionTrigger by remember { mutableStateOf<MovieReaction?>(null) }

    var showReviews by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    // Disable overlay drag when sheet is visible
    val dragEnabled = scrollState.value == 0 && !isExpanded && !showReviews

    Box(modifier = Modifier.fillMaxSize()) {

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
                            if (offsetY.value > dismissThresholdPx) {
                                onDismiss()
                            } else if (offsetY.value < reviewThresholdPx) {
                                offsetY.animateTo(0f)
                                showReviews = true
                            } else {
                                offsetY.animateTo(0f)
                            }
                        }
                    })
        ) {

            // Background
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient
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
            ) {
                RainingEmoji(
                    trigger = reactionTrigger, onAnimationFinished = { reactionTrigger = null })
            }


            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {

                Row(modifier = Modifier.fillMaxWidth().offset(y = (-20).dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    // ⭐ Rating
                    StarRating(
                        modifier = Modifier
                            .padding(end = 12.dp, top = 12.dp)
//                        .align(Alignment.TopStart)
                        ,
                        rating = movie.userRating,
                        onRatingChanged = { newRating ->
                            onRate(movie.id, newRating)
                        })

                    // ❤️ Favorite
                    OutlinedButton(
                        onClick = { onToggleFavorite(movie) },
                        modifier = Modifier
                            .padding(end = 12.dp, top = 12.dp)
//                        .align(Alignment.TopEnd)
                        ,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                    {
                        Text(
                            if (movie.isFavorite) "Remove from" else "Mark as",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = if (movie.isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (movie.isFavorite) Color.Red else LocalContentColor.current
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                MovieReactionBar(
                    reactions = movie.userReactions, onReact = { reaction, selected ->
                        reactionTrigger = reaction
                        onReact(movie.id, reaction, selected)
                    })
                ExpandableStackedColumn(movie)
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
                GlitterText(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(Color.White)
                        .offset(y = 15.dp)
                        .clickable(enabled = true, onClick = {
                            showReviews = true
                        }), text = " \u2B06\u2B06\u2B06 View Reviews  \u2B06\u2B06\u2B06\u2B06"
                )
            }
        }


        if (showReviews) {
            ModalBottomSheet(onDismissRequest = {
                showReviews = false
            }, sheetState = sheetState, dragHandle = null) {
                CommentsAndRepliesSheet()
            }
        }
    }
}
