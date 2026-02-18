package com.labs.systemdesignandroid.feature.comments.data.utils

import com.labs.systemdesignandroid.feature.comments.data.Comment
import com.labs.systemdesignandroid.feature.comments.data.PageResult

fun paginateRoots(
    allComments: List<Comment>,
    page: Int,
    pageSize: Int
): PageResult {

    val roots = allComments.filter { it.parentId == null }

    val fromIndex = page * pageSize
    val toIndex = (fromIndex + pageSize).coerceAtMost(roots.size)

    if (fromIndex >= roots.size) {
        return PageResult(emptyList(), null, false)
    }

    val pageRoots = roots.subList(fromIndex, toIndex)
    val pageRootIds = pageRoots.map { it.id }

    val related = allComments.filter {
        it.id in pageRootIds || it.parentId in pageRootIds
    }

    val nextPage = if (toIndex < roots.size) page + 1 else null

    return PageResult(
        data = related,
        nextPage = nextPage,
        hasMore = nextPage != null
    )
}
