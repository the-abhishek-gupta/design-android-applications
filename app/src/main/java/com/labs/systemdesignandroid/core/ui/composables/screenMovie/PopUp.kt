package com.labs.systemdesignandroid.core.ui.composables.screenMovie


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.labs.systemdesignandroid.domain.MovieReaction


@Composable
fun PopUp(
    onReact: (MovieReaction) -> Unit, onDismiss: () -> Unit, TAG: String = "abhi.PopUp"
) {
    var animateItems by remember { mutableStateOf(false) }
    var isDismissing by remember { mutableStateOf(false) }

    Popup(
        onDismissRequest = onDismiss, properties = PopupProperties(focusable = true)
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .zIndex(50F),
            shape = RoundedCornerShape(50.dp),
            shadowElevation = 10.dp,
            color = Color.Transparent.copy(alpha = 0.55f),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MovieReaction.entries.forEachIndexed { index, reaction ->
                    ReactionItem(reaction,index, onReactionClick = { onReact(reaction) })
//                    ReactionButton(reaction, index, animateItems, onReactionClick = { onDismiss() })
                }
            }
        }
    }
}