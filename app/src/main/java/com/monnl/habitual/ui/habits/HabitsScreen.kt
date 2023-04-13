package com.monnl.habitual.ui.habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.data.models.HabitType
import com.monnl.habitual.ui.components.HabitLinearProgressBar
import com.monnl.habitual.ui.components.HabitsFloatingActionButton
import com.monnl.habitual.ui.components.HabitsTabRow
import com.monnl.habitual.ui.habitsScreenCategories
import kotlinx.coroutines.launch
import java.util.*


@Composable
fun HabitsScreen(
    onHabitClick: (String) -> Unit
) {
    val viewModel: HabitsViewModel = viewModel()

    HabitsBottomSheetScaffold(
        viewModel = viewModel,
        content = { innerPadding ->
            HabitsScaffold(
                contentPadding = innerPadding,
                onHabitClick = onHabitClick,
                viewModel = viewModel
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitsScaffold(
    contentPadding: PaddingValues,
    onHabitClick: (String) -> Unit,
    viewModel: HabitsViewModel
) {
    val suggestedHabits by viewModel.suggestedHabits.collectAsState(emptyList())

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.padding(contentPadding),
        floatingActionButton = { HabitsFloatingActionButton(onHabitClick) }
    )
    { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column {
                HabitsTabRow(
                    categories = habitsScreenCategories,
                    selectedCategoryIndex = pagerState.currentPage,
                    onCategorySelected = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
                HabitsTypeHorizontalPager(
                    state = pagerState,
                    habits = suggestedHabits,
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
            val habitsToShow = habits.filter { habitsScreenCategories[index].filterPredicate(it) }
            HabitsList(
                habits = habitsToShow,
                onHabitClick = onHabitClick
            )
        }
    }
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
                style = MaterialTheme.typography.titleLarge
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
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = "${habit.completeTimes} / ${habit.targetTimes} in ${habit.period} days",
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.Top)
                )
            }
        }
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
