package com.labs.systemdesignandroid.feature.comments.data

data class PageResult(
    val data: List<Comment>,
    val nextPage: Int?,
    val hasMore: Boolean
)
