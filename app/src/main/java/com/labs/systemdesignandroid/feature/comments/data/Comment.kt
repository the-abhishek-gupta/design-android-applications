package com.labs.systemdesignandroid.feature.comments.data

data class Comment(
    val id: String,
    val parentId: String?,
    val userName: String,
    val message: String,
    val isLikedByUser: Boolean = false,
    val likeCount: Int,
    val timestamp: Long
)
