package com.labs.systemdesignandroid.feature.comments.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.labs.systemdesignandroid.feature.comments.viewmodel.CommentViewModel

@Composable
fun CommentsAndRepliesSheet(
    viewModel: CommentViewModel = viewModel(),
) {

    val listState = rememberLazyListState()
    val uiList = viewModel.uiList
    var replyingTo by remember { mutableStateOf<String?>(null) }
    var replyText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // 1. Auto-open keyboard when the composable enters the screen
    LaunchedEffect(replyingTo) {
        if (replyingTo != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= uiList.size - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && viewModel.hasMore && !viewModel.isLoading) {
            viewModel.loadNextPage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize().imePadding().pointerInput(replyingTo) {
                detectTapGestures(onTap = {
                    if (replyingTo != null) {
                        replyingTo = null
                        replyText = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                })
            }
    ) {

        if (uiList.isEmpty() && !viewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No reviews yet. Be the first one 👀")
            }
            return@Column
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(top = 20.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            items(
                items = uiList,
                key = { it.id }
            ) { item ->

                CommentItem(
                    comment = item,
                    isReplyTarget = item.id == replyingTo,
                    onToggleReplies = viewModel::toggleExpansion,
                    onReplyClick = { commentId ->
                        replyingTo = commentId
                    },
                    onLikeClick =  viewModel::toggleLike,
                )
            }

            if (viewModel.isLoading) {
                item("loader") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        if (replyingTo != null) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = 6.dp) // push slightly outside chip corner
                        .align(Alignment.Top)
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF0000))
                        .clickable{
                            replyingTo = null
                            replyText = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFFFFFFFF)
                    )
                }


                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = replyText,
                    onValueChange = { replyText = it },
                    modifier = Modifier.weight(1f).focusRequester(focusRequester),
                    placeholder = { Text("Write a reply...") },
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        disabledBorderColor = Color.Gray,
                        errorBorderColor = Color.Red
                    )
                )

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (replyText.isNotBlank()) {
                            viewModel.addReply(
                                parentId = replyingTo!!,
                                message = replyText.trim()
                            )
                            replyText = ""
                            replyingTo = null
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                    enabled = replyText.isNotBlank()
                ) {
                    Text("Send")
                }
            }
        }

    }
}
