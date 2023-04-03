package com.monnl.habitual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.monnl.habitual.ui.components.HabitualTopAppBar
import com.monnl.habitual.ui.navigation.*
import com.monnl.habitual.ui.theme.HabitualTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HabitualMainComposable() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitualMainComposable() {
    HabitualTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            habitualDestinationScreens.find { it.route == currentDestination?.route }
                ?: Habits

        val drawerSheetItems = habitualTabDrawerScreens
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet {

                    Spacer(Modifier.height(12.dp))

                    drawerSheetItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.route
                                )
                            },
                            label = { Text(text = item.name) },
                            selected = item == currentScreen,
                            onClick = {
                                navController.navigateSingleTopTo(item.route)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    HabitualTopAppBar(
                        currentScreen = currentScreen,
                        onNavIconClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
            )
            { innerPadding ->
                HabitualNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
