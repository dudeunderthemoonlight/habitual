package com.monnl.habitual.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface HabitualDestination {
    val icon: ImageVector
    val route: String
    val name: String
}

object Habits : HabitualDestination {
    override val icon = Icons.Filled.List
    override val route = "habits"
    override val name = "Your habits"
}

object Info : HabitualDestination {
    override val icon = Icons.Filled.Info
    override val route = "information"
    override val name = "Info"
}

object SingleHabit : HabitualDestination {
    override val icon = Icons.Filled.Edit
    override val route = "single_habit"
    override val name = "Your Habit"

    const val habitKey = "habit_id"

    val routeWithArgs = "$route?$habitKey={$habitKey}"

    val arguments = listOf(
        navArgument(habitKey) {
            nullable = true
            //defaultValue = null
            type = NavType.StringType
        }
    )
}

val habitualTabDrawerScreens = listOf(Habits, Info)
val habitualDestinationScreens = listOf(Habits, Info, SingleHabit)