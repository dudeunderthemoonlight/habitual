package com.monnl.habitual.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.monnl.habitual.ui.navigation.HabitualDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitualTopAppBar(
    currentScreen: HabitualDestination,
    onNavIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text(currentScreen.name) },
        navigationIcon = {
            IconButton(onClick = { onNavIconClick() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open navigation drawer"
                )
            }
        }
    )
}