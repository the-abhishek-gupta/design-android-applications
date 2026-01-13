package com.labs.systemdesignandroid.core.ui.composables.screenMovie


import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.labs.systemdesignandroid.domain.model.MovieModel


@Composable
fun MovieItem(
    movie: MovieModel,
    onToggleFavorite: (MovieModel) -> Unit,
    onToggleWatchlist: (MovieModel) -> Unit,
    modifier: Modifier = Modifier
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
                .size(60.dp, 60.dp)
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
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${movie.year} • ${movie.genres.joinToString(", ")}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
//        IconButton(onClick = { onToggleWatchlist(movie) }) {
//            Icon(
//                imageVector = if (movie.isInWatchlist) Icons.Default.Check
//                else Icons.Default.Add, contentDescription = null
//            )
//        }
//
//        IconButton(onClick = { onToggleFavorite(movie) }) {
//            Icon(
//                imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
//                contentDescription = "Favorite",
//                tint = if (movie.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
    }
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = movie.name,
//                style = MaterialTheme.typography.titleMedium,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
//            )
//
//            Text(
//                text = "${movie.year} • ⭐ ${movie.rating}",
//                style = MaterialTheme.typography.bodySmall
//            )
//        }
//
//        IconButton(onClick = { onToggleWatchlist(movie) }) {
//            Icon(
//                imageVector = if (movie.isInWatchlist) Icons.Default.Check
//                else Icons.Default.Add, contentDescription = null
//            )
//        }
//
//        IconButton(onClick = { onToggleFavorite(movie) }) {
//            Icon(
//                imageVector = if (movie.isFavorite) Icons.Default.Favorite
//                else Icons.Default.FavoriteBorder,
//                tint = if (movie.isFavorite) Color.Red else LocalContentColor.current,
//                contentDescription = null
//            )
//        }
//    }
}
