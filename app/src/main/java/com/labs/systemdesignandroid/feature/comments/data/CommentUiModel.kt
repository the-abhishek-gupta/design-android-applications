package com.labs.systemdesignandroid.feature.comments.data

data class CommentUiModel(
    val id: String,
    val parentId: String?,
    val userName: String,
    val message: String,
    var isLikedByUser: Boolean,
    val likeCount: Int,
    val timestamp: Long,
    val depth: Int,
    val isExpanded: Boolean,
    val hasReplies: Boolean
){

}