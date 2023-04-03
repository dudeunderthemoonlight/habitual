package com.monnl.habitual.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.monnl.habitual.R

@Composable
fun HabitsFloatingActionButton(
    onHabitClick: (String) -> Unit
) {
    FloatingActionButton(
        onClick = { onHabitClick("") }
    ) {
        Icon(Icons.Filled.Add, stringResource(R.string.add_button_description))
    }
}
