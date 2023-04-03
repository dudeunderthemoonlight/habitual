package com.monnl.habitual.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.monnl.habitual.ui.navigation.HabitualDestination
import com.monnl.habitual.R

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
                    contentDescription = stringResource(R.string.top_appbar_description)
                )
            }
        }
    )
}