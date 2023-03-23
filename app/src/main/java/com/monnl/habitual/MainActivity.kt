package com.monnl.habitual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.models.Habit
import com.monnl.habitual.data.models.models.HabitPriority
import com.monnl.habitual.data.models.models.HabitType
import com.monnl.habitual.ui.HabitCategoryTab
import com.monnl.habitual.ui.habitsScreenCategories
import com.monnl.habitual.ui.navigation.*
import com.monnl.habitual.ui.theme.HabitualTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HabitualApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitualApp() {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HabitsScreen(
    habits: List<Habit> = HabitsDataSource.habits,
    onHabitClick: (String) -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = { HabitsFloatingActionButton(onHabitClick) }
    )
    { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                HabitsTabRow(
                    categories = habitsScreenCategories,
                    selectedCategoryIndex = pagerState.currentPage,
                    onCategorySelected = { index ->
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }
                )
                HabitsTypeHorizontalPager(
                    state = pagerState,
                    habits = habits,
                    onHabitClick = onHabitClick
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitsTypeHorizontalPager(
    state: PagerState,
    habits: List<Habit>,
    onHabitClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            pageCount = habitsScreenCategories.size,
            state = state
        ) { index ->
            val filterPredicate = habitsScreenCategories[index].filterPredicate
            val habitsToShow = remember { habits.filter { filterPredicate(it) } }

            HabitsList(
                habits = habitsToShow,
                onHabitClick = onHabitClick
            )
        }
    }
}

@Composable
fun InfoScreen() {
    Text(text = "This is information screen")
}

@Composable
fun HabitsList(
    habits: List<Habit>,
    onHabitClick: (String) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(habits) { habit ->
            HabitCard(
                habit = habit,
                onHabitClick = onHabitClick
            )
        }
    }
}

@Composable
fun HabitsTabRow(
    categories: List<HabitCategoryTab>,
    selectedCategoryIndex: Int,
    onCategorySelected: (index: Int) -> Unit = { }
) {
    TabRow(
        selectedTabIndex = selectedCategoryIndex
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedCategoryIndex,
                onClick = { onCategorySelected(index) }
            ) {
                Text(text = category.text)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitCard(
    habit: Habit,
    onHabitClick: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = { isExpanded = !isExpanded },
                onLongClick = { onHabitClick(habit.id) }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, start = 14.dp, bottom = 14.dp, end = 14.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = habit.name,
                style = typography.titleLarge
            )
            Text(
                text = habit.description,
                maxLines = if (isExpanded) 5 else 1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HabitLinearProgressBar(
                    habit = habit,
                    modifier = Modifier
                        .weight(2.0f)
                        .align(CenterVertically)
                )
                Text(
                    text = "${habit.completeTimes} / ${habit.targetTimes} in ${habit.period} days",
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Top)
                )
            }
        }
    }
}

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

@Composable
fun HabitLinearProgressBar(habit: Habit, modifier: Modifier) {

    val targetProgress = habit.completeTimes?.toFloat()!! / habit.targetTimes?.toFloat()!!

    val progress = remember { Animatable(0f) }

    LaunchedEffect(true) {
        progress.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(1200)
        )
    }

    Column(modifier = modifier) {
        LinearProgressIndicator(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth(),
            strokeCap = StrokeCap.Round,
            progress = progress.value
        )
    }
}


@Preview
@Composable
fun PreviewHabitList() {
    HabitsList(
        habits = listOf(
            Habit(
                id = UUID.randomUUID().toString(),
                name = "fitness",
                description = "go to the gym",
                priority = HabitPriority.High,
                type = HabitType.Good,
                targetTimes = 3,
                completeTimes = 0,
                period = 7,
                color = Color.Blue.value.toInt()
            ),
            Habit(
                id = UUID.randomUUID().toString(),
                name = "food",
                description = "eat 3 times a day for a week",
                priority = HabitPriority.Medium,
                type = HabitType.Good,
                targetTimes = 7,
                completeTimes = 0,
                period = 7,
                color = Color.Blue.value.toInt()
            ), Habit(
                id = UUID.randomUUID().toString(),
                name = "sleep",
                description = "go to bed earlier than 12 pm",
                priority = HabitPriority.High,
                type = HabitType.Good,
                targetTimes = 7,
                completeTimes = 0,
                period = 7,
                color = Color.Blue.value.toInt()
            )
        )
    )
}

