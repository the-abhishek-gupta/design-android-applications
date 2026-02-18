package com.labs.systemdesignandroid.core.ui.composables.utils

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableStackedColumnTest(

) {
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
    ) {
        // 1. Top Text
        Text("Top Item", modifier = Modifier.padding(8.dp))

        // 2. Middle Expandable & Scrollable Area
        Box(
            modifier = Modifier
                .padding(8.dp)
                // fill = false lets it stay small if text is short
                .weight(1f, fill = false)
                .clickable { isExpanded = !isExpanded }
                .animateContentSize()) {
            Text(
                text = "Your very long multi-line text... ".repeat(50),
                modifier = Modifier.verticalScroll(scrollState),
                // Expanded state has no line limit, initial state has 4
                maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 3. Bottom Text
        Text("Bottom Item", modifier = Modifier.padding(8.dp))
    }
}
