package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@Composable
fun StarRating(
    rating: Int, // 0..5
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var editing by rememberSaveable { mutableStateOf(false) }
    var sliderValue by rememberSaveable(rating) { mutableFloatStateOf(rating.toFloat()) }

    // keep slider in sync when DB updates rating
    LaunchedEffect(rating) {
        if (!editing) sliderValue = rating.toFloat()
    }

    Column(modifier = modifier) {

        // Collapsed view (static)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    editing = true
                    sliderValue = rating.toFloat()
                }
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StaticStars(rating = rating)
            Spacer(Modifier.width(10.dp))
            Text(
                text = if (rating == 0) "Tap to rate" else "$rating/5",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit rating",
                tint = Color.White.copy(alpha = 0.85f)
            )
        }

        // Expanded slider editor
        AnimatedVisibility(
            visible = editing,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(12.dp)
            ) {
                // Live preview stars
                val previewRating = sliderValue.roundToInt().coerceIn(0, 5)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StaticStars(rating = previewRating)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = if (previewRating == 0) "0/5" else "$previewRating/5",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "Clear",
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                sliderValue = 0f
                                onRatingChanged(0)
                                editing = false
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    valueRange = 0f..5f,
                    steps = 4 // gives 0,1,2,3,4,5
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        // cancel → revert and collapse
                        sliderValue = rating.toFloat()
                        editing = false
                    }) {
                        Text("Cancel", color = Color.White)
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onRatingChanged(previewRating)
                            editing = false
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@Composable
private fun StaticStars(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val star = index + 1
            val filled = star <= rating

            Icon(
                imageVector = if (filled) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = if (filled) Color(0xFFFFC107) else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(2.dp))
        }
    }
}

