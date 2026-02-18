package com.labs.systemdesignandroid.core.ui.composables.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.labs.systemdesignandroid.domain.model.MovieModel


@Composable
fun ExpandableStackedColumn(
    movie: MovieModel,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
    ) {
        // 1. Top Movie Name
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
            Text(
                "${movie.year}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                "${movie.durationMinutes} min",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = movie.genres.joinToString(" • "),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primaryContainer,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(20.dp))
        // 2. Middle Expandable & Scrollable Area
        Box(
            modifier = Modifier
                .padding(8.dp)
                // fill = false lets it stay small if text is short
                .weight(1f, fill = false)
                .clickable { isExpanded = !isExpanded }
                .animateContentSize()) {
            Text(
                text = movie.description.repeat(50),
                modifier = Modifier.verticalScroll(scrollState),
                // Expanded state has no line limit, initial state has 4
                maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 26.sp,
            )
        }
    }
}
