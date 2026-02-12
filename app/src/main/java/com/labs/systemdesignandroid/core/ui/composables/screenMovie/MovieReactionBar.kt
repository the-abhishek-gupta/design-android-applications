package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.labs.systemdesignandroid.domain.MovieReaction

@Composable
fun MovieReactionBar(
    reactions: Set<MovieReaction>,
    onReact: (MovieReaction, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {

        OutlinedButton(
            onClick = { expanded = true }, shape = RoundedCornerShape(20.dp)
        ) {
            Text(reactions.ifEmpty { MovieReaction.entries }
                .joinToString("", limit = 3, truncated = "") { it.emoji })


        }

        if (expanded) {
            FloatingReactionBar(
                onReact = { reaction ->
                    val selected = reaction in reactions
                    onReact(reaction, selected)
                    expanded = false
                },
                onDismiss = { expanded = false })
        }
    }
}


