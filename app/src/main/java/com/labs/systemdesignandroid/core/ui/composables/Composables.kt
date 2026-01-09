package com.labs.systemdesignandroid.core.ui.composables

import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.labs.systemdesignandroid.domain.Movie
import com.labs.systemdesignandroid.domain.SortOrder
import kotlinx.coroutines.launch

@Composable
fun MovieList(
    movies: List<Movie>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onMovieClick: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    onSortOrderSelected: (SortOrder) -> Unit = {},
    genres: List<String>,
    selectedGenres: Set<String>,
    onToggleGenre: (String) -> Unit
) {
    var isSearchExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            if (isSearchExpanded) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Search Movie") },
                    leadingIcon = {
                        IconButton(onClick = {
                            isSearchExpanded = false
                            onSearchQueryChanged("")
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    singleLine = true
                )
            } else {
                Text(
                    text = "Movies",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                IconButton(onClick = { isSearchExpanded = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
            SortMenu { order ->
                onSortOrderSelected(order)
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        GenreChipsRow(
            genres = genres,
            selectedGenres = selectedGenres,
            onToggle = onToggleGenre
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            state = listState, modifier = Modifier.fillMaxWidth()
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieItem(
                    movie = movie,
                    onToggleFavorite = { onToggleFavorite(movie) },
                    modifier = Modifier
                        .animateItem()
                        .clickable { onMovieClick(movie) })
                HorizontalDivider(modifier = Modifier.animateItem())
            }
        }
    }
}

@Composable
fun GenreChipsRow(
    genres: List<String>,
    selectedGenres: Set<String>,
    onToggle: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genres) { genre ->
            ElevatedFilterChip(
                selected = genre in selectedGenres,
                onClick = { onToggle(genre) },
                label = { Text(genre) }
            )
        }
    }
}

@Composable
fun MovieDetailOverlay(
    movie: Movie,
    onToggleFavorite: (Movie) -> Unit,
    onToggleWatchlist: (Movie) -> Unit,
    onDismiss: () -> Unit
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
            .draggable(orientation = Orientation.Vertical, state = rememberDraggableState { delta ->
                scope.launch {
                    offsetY.snapTo(
                        (offsetY.value + delta * 0.6f).coerceIn(-600f, 600f)
                    )
                }
            }, onDragStopped = { velocity ->
                scope.launch {
                    if (kotlin.math.abs(offsetY.value) > dismissThreshold || kotlin.math.abs(
                            velocity
                        ) > 1500f
                    ) {
                        onDismiss()
                    } else {
                        offsetY.animateTo(0f)
                    }
                }
            })
    ) {

        // Background image
        AsyncImage(
            model = movie.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
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

        // Mark as Favorite Button
        OutlinedButton(
            onClick = { onToggleFavorite(movie) },
            modifier = Modifier
                .wrapContentSize()
                .padding(end = 12.dp, top = 12.dp)
                .align(Alignment.TopEnd),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (movie.isFavorite) "Remove from" else "Mark as",
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if (movie.isFavorite) Color.Red else LocalContentColor.current
            )
        }
        // Details content
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
                Text(
                    text = "${movie.year}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "${movie.durationMinutes} min",
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

            val text = movie.description
            var clickEnabled by remember { mutableStateOf(false) }

            Text(
                text = "$text $text $text $text $text $text $text $text $text $text ",
                maxLines = if (clickEnabled) Int.MAX_VALUE else 4,
                overflow = if (!clickEnabled) TextOverflow.Ellipsis else TextOverflow.Visible,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 26.sp,
                modifier = Modifier
                    .height(120.dp)
                    .verticalScroll(rememberScrollState())
                    .clickable {
                        clickEnabled = !clickEnabled
                    })

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Watchlist Button
                if (movie.isInWatchlist) {
                    Button(
                        onClick = { onToggleWatchlist(movie) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
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
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
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

@Composable
fun AddMovieDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Add New Movie") }, text = {
        Column {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Genre") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }, confirmButton = {
        Button(
            onClick = { onConfirm(title, genre) },
            enabled = title.isNotBlank() && genre.isNotBlank()
        ) {
            Text("Add")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

@Composable
fun SortMenu(onSortOrderSelected: (SortOrder) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Sort Options")
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("None") }, onClick = {
                onSortOrderSelected(SortOrder.NONE)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Title (Asc)") }, onClick = {
                onSortOrderSelected(SortOrder.NAME_ASC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Title (Desc)") }, onClick = {
                onSortOrderSelected(SortOrder.NAME_DESC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Genre (Asc)") }, onClick = {
                onSortOrderSelected(SortOrder.GRADE_ASC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Genre (Desc)") }, onClick = {
                onSortOrderSelected(SortOrder.GRADE_DESC)
                expanded = false
            })
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie, onToggleFavorite: (Movie) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = movie.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp, 90.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${movie.year} • ${movie.genres.joinToString(", ")}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = movie.rating.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        IconButton(onClick = { onToggleFavorite(movie) }) {
            Icon(
                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (movie.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
