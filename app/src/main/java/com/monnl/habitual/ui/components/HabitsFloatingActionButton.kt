package com.monnl.habitual.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun HabitsFloatingActionButton(
    onHabitClick: (String) -> Unit
) {
    FloatingActionButton(
        onClick = { onHabitClick("") }
    ) {
        Icon(Icons.Filled.Add, "Create new habit")
    }
}
