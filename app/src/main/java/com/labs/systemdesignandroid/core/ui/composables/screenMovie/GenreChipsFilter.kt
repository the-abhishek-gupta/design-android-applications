package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GenreChipsFilter(
    genres: List<String>, selectedGenres: Set<String>, onToggle: (String) -> Unit
) {
    val TAG = "abhi.GenreChipsFilter"
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = genres, key = { it }) { genre ->
            GenreChipWithRemoveBadge(
                genre = genre, selected = genre in selectedGenres, onToggle = { onToggle(genre) })
        }
    }
}

@Composable
private fun GenreChipWithRemoveBadge(
    genre: String, selected: Boolean, onToggle: () -> Unit
) {
    Box(modifier = Modifier.wrapContentSize()) {

        FilterChip(
            shape = RoundedCornerShape(20.dp),
            selected = selected,
            onClick = onToggle,
            label = { Text(genre) })

        if (selected) {
            // Top-right circular "X" badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp) // push slightly outside chip corner
                .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove $genre",
                    modifier = Modifier.size(12.dp),
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    }
}
