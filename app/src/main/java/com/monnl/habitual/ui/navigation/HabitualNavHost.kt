package com.monnl.habitual.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.monnl.habitual.ui.habits.HabitsScreen
import com.monnl.habitual.ui.habits.SingleHabitScreen
import com.monnl.habitual.ui.info.InfoScreen


@Composable
fun HabitualNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Habits.route,
        modifier = modifier
    ) {
        composable(route = Habits.route) {
            HabitsScreen(
                onHabitClick = { habitId ->
                    navController.navigateToSingleHabit(habitId)
                }
            )
        }

        composable(route = Info.route) {
            InfoScreen()
        }

        composable(
            route = SingleHabit.routeWithArgs,
            arguments = SingleHabit.arguments
        ) { navBackStackEntry ->
            val habitId = navBackStackEntry.arguments?.getString(SingleHabit.habitKey)

            SingleHabitScreen(
                habitId = habitId,
                onSaveButtonClick = { navController.popBackStack() }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // avoid large backstack
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // avoid multiple copies of the same destination when
        // reselecting item
        launchSingleTop = true
        // restore state when reselecting the previous selected item
        restoreState = true
    }

private fun NavHostController.navigateToSingleHabit(habitId: String) {
    val route = "${SingleHabit.route}?${SingleHabit.habitKey}=$habitId"
    this.navigateSingleTopTo(route)
}