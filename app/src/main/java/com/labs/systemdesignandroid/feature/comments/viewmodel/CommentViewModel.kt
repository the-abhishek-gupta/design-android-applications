package com.labs.systemdesignandroid.feature.comments.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labs.systemdesignandroid.feature.comments.data.Comment
import com.labs.systemdesignandroid.feature.comments.data.CommentUiModel
import com.labs.systemdesignandroid.feature.comments.data.utils.generateProductionComments
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


class CommentViewModel : ViewModel() {
    private val allComments = generateProductionComments(rootCount = 200)

    private var currentPage = 0
    private val pageSize = 10

    private val _comments = mutableStateListOf<Comment>()
    val comments: List<Comment> get() = _comments

    private val _uiList = mutableStateListOf<CommentUiModel>()
    val uiList: List<CommentUiModel> get() = _uiList

    private val expandedSet = mutableSetOf<String>()

    // Fast lookup maps
    private val commentIndexMap = mutableMapOf<String, Int>()
    private val uiIndexMap = mutableMapOf<String, Int>()
    private val childrenMap = mutableMapOf<String?, MutableList<Comment>>()

    var isLoading by mutableStateOf(false)
        private set

    var hasMore by mutableStateOf(true)
        private set

    init {
        buildChildrenMap()
        loadNextPage()
    }


    private fun buildChildrenMap() {
        allComments.forEach { comment ->
            childrenMap.getOrPut(comment.parentId) { mutableListOf() }
                .add(comment)
        }
    }


    fun loadNextPage() {
        if (isLoading || !hasMore) return

        viewModelScope.launch {
            isLoading = true
            delay(500)

            val roots = childrenMap[null].orEmpty()

            val from = currentPage * pageSize
            val to = (from + pageSize).coerceAtMost(roots.size)

            if (from >= roots.size) {
                hasMore = false
                isLoading = false
                return@launch
            }

            val newRoots = roots.subList(from, to)

            newRoots.forEach { root ->
                addUiItem(root, depth = 0)
            }

            currentPage++
            hasMore = to < roots.size
            isLoading = false
        }
    }


    private fun addUiItem(comment: Comment, depth: Int) {
        val uiModel = comment.toUiModel(depth)
        _uiList.add(uiModel)
        uiIndexMap[comment.id] = _uiList.lastIndex
        commentIndexMap[comment.id] = _comments.size
        _comments.add(comment)
    }


    fun toggleExpansion(id: String) {

        val index = uiIndexMap[id] ?: return

        if (expandedSet.contains(id)) {
            collapseSubtree(index)
            expandedSet.remove(id)
        } else {
            expandSubtree(id, index)
            expandedSet.add(id)
        }
    }

    private fun expandSubtree(parentId: String, parentIndex: Int) {
        val parentDepth = _uiList[parentIndex].depth
        val children = childrenMap[parentId].orEmpty()

        var insertIndex = parentIndex + 1

        children.forEach { child ->
            val ui = child.toUiModel(parentDepth + 1)
            _uiList.add(insertIndex, ui)
            insertIndex++
        }

        rebuildUiIndexMap()
    }

    private fun collapseSubtree(parentIndex: Int) {
        val parentDepth = _uiList[parentIndex].depth
        var removeIndex = parentIndex + 1

        while (
            removeIndex < _uiList.size &&
            _uiList[removeIndex].depth > parentDepth
        ) {
            _uiList.removeAt(removeIndex)
        }

        rebuildUiIndexMap()
    }

    private fun rebuildUiIndexMap() {
        uiIndexMap.clear()
        _uiList.forEachIndexed { index, item ->
            uiIndexMap[item.id] = index
        }
    }


    fun addReply(parentId: String, message: String) {

        val newComment = Comment(
            id = UUID.randomUUID().toString(),
            parentId = parentId,
            userName = "You",
            message = message,
            likeCount = 0,
            timestamp = System.currentTimeMillis(),
            isLikedByUser = false
        )

        childrenMap.getOrPut(parentId) { mutableListOf() }
            .add(0, newComment)

        if (!expandedSet.contains(parentId)) {
            toggleExpansion(parentId)
        } else {
            val parentIndex = uiIndexMap[parentId] ?: return
            val depth = _uiList[parentIndex].depth
            _uiList.add(parentIndex + 1, newComment.toUiModel(depth + 1))
            rebuildUiIndexMap()
        }
    }


    fun toggleLike(commentId: String) {

        val uiIndex = uiIndexMap[commentId] ?: return
        val oldUi = _uiList[uiIndex]

        val updatedUi = if (oldUi.isLikedByUser) {
            oldUi.copy(
                isLikedByUser = false,
                likeCount = (oldUi.likeCount - 1).coerceAtLeast(0)
            )
        } else {
            oldUi.copy(
                isLikedByUser = true,
                likeCount = oldUi.likeCount + 1
            )
        }

        _uiList[uiIndex] = updatedUi
    }


    private fun Comment.toUiModel(depth: Int) =
        CommentUiModel(
            id = id,
            parentId = parentId,
            userName = userName,
            message = message,
            likeCount = likeCount,
            isLikedByUser = isLikedByUser,
            depth = depth,
            isExpanded = expandedSet.contains(id),
            hasReplies = childrenMap[id]?.isNotEmpty() == true,
            timestamp = timestamp
        )
}


