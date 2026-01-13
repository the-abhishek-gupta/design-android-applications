package com.labs.systemdesignandroid.core.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.labs.systemdesignandroid.domain.SortOrder

@Composable
fun SortMenu(onSortOrderSelected: (SortOrder) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Sort Options")
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Name ↑") }, onClick = {
                onSortOrderSelected(SortOrder.NAME_ASC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Name ↓") }, onClick = {
                onSortOrderSelected(SortOrder.NAME_DESC)
                expanded = false
            })
            DropdownMenuItem(text = { Text("Ratings ↓") }, onClick = {
                onSortOrderSelected(SortOrder.RATING_DESC)
                expanded = false
            })
        }
    }
}
