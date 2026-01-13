package com.labs.systemdesignandroid.core.ui.composables.screenMovie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.labs.systemdesignandroid.MovieFilter
import com.labs.systemdesignandroid.core.ui.composables.SortMenu
import com.labs.systemdesignandroid.domain.SortOrder

@Composable
fun MovieTopBar(
    searchQuery: String,
    selectedTab: MovieFilter,
    onTabSelected: (MovieFilter) -> Unit,
    onSortSelected: (SortOrder) -> Unit,
    onSearchChanged: (String) -> Unit,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (isSearchExpanded) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search Movie") },
                leadingIcon = {
                    IconButton(onClick = {
                        isSearchExpanded = false
                        onSearchChanged("")
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                )
            )
        } else {
            MovieFilter.entries.forEach { filter ->
                FilterChip(
                    shape = RoundedCornerShape(20.dp),
                    selected = selectedTab == filter,
                    onClick = { onTabSelected(filter) },
                    label = { Text(filter.name.lowercase().replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { isSearchExpanded = true }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

        // Sort
        SortMenu(onSortSelected)
    }
}
