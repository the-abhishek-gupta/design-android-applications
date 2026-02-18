package com.labs.systemdesignandroid.feature.comments.utils

import com.labs.systemdesignandroid.feature.comments.data.Comment
import com.labs.systemdesignandroid.feature.comments.data.CommentUiModel
import kotlin.collections.listOf
import kotlin.collections.random

fun buildThreadedUiList(
    comments: List<Comment>, expandedMap: Map<String, Boolean> = emptyMap()
): List<CommentUiModel> {

    val grouped = comments.groupBy { it.parentId }
    val result = mutableListOf<CommentUiModel>()

    fun addComments(parentId: String?, depth: Int) {
        grouped[parentId]?.forEach { comment ->

            val children = grouped[comment.id].orEmpty()
            val isExpanded = expandedMap[comment.id] ?: true

            result.add(
                CommentUiModel(
                    id = comment.id,
                    parentId = comment.parentId,
                    userName = comment.userName,
                    message = comment.message,
                    likeCount = comment.likeCount,
                    isLikedByUser = comment.isLikedByUser,
                    timestamp = comment.timestamp,
                    depth = depth,
                    isExpanded = isExpanded,
                    hasReplies = children.isNotEmpty()
                )
            )

            if (isExpanded) {
                addComments(comment.id, depth + 1)
            }
        }
    }

    addComments(parentId = null, depth = 0)

    return result
}


