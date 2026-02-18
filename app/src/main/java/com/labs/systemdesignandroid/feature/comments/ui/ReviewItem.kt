package com.labs.systemdesignandroid.feature.comments.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.labs.systemdesignandroid.feature.comments.data.CommentUiModel


@Composable
fun CommentItem(
    comment: CommentUiModel,
    isReplyTarget: Boolean,
    onToggleReplies: (String) -> Unit,
    onReplyClick: (String) -> Unit,
    onLikeClick: (String) -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isReplyTarget)
            MaterialTheme.colorScheme.primary
        else
            Color.Transparent,
        label = "replyHighlight"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isReplyTarget)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        else
            Color.Transparent,
        label = "replyBackground"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (comment.depth * 16).dp, top = 8.dp, bottom = 8.dp, end = 0.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .border(
                width = if (isReplyTarget) 1.5.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .animateContentSize()
    ) {

        if (isReplyTarget) {
            Text(
                text = "Replying...",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
        }

        Row {
            Text(
                text = comment.userName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            if (comment.hasReplies) {
                Spacer(Modifier.width(16.dp))
                Icon(
                    if (comment.isExpanded) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    if (comment.isExpanded) "Hide Replies" else "Show Replies",
                    modifier = Modifier.clickable {
                        onToggleReplies(comment.id)
                    })

            }
            Icon(
                if (comment.isLikedByUser) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Like",
                tint = Color.Red,
                modifier = Modifier.clickable {
                    onLikeClick(comment.id)
                })
            Spacer(Modifier.width(14.dp))
            Icon(
                Icons.AutoMirrored.Filled.Message, "Reply", tint = Color.Blue,
                modifier = Modifier.clickable {
                    onReplyClick(comment.id)
                })


        }
        Text(comment.message)

    }
}
