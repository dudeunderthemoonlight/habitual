package com.monnl.habitual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.data.models.HabitType
import com.monnl.habitual.ui.theme.HabitualTheme
import java.util.*

class MainActivity : ComponentActivity() {

    var habits = HabitsDataSource.habits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitualTheme {
                HabitualApp(habits = habits) {
                    navigateToHabitActivity(it)
                }
            }
        }
    }

    private fun navigateToHabitActivity(habit: Habit?) {
        startActivity(newHabitIntent(this, habit))
    }

    private fun newHabitIntent(context: Context, habit: Habit?) =
        Intent(context, HabitActivity::class.java).apply {
            putExtra(HabitActivity.HABIT_KEY, habit)
        }
}

@Composable
fun HabitualApp(
    habits: List<Habit>,
    navigateToHabitDetails: (Habit?) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HabitListScreen(habits, navigateToHabitDetails)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    habits: List<Habit>,
    navigateToHabitDetails: (Habit?) -> Unit
) {
    Scaffold(
        topBar = { HabitsScreenTopAppBar() },
        floatingActionButton = { HabitsFloatingActionButton(navigateToHabitDetails) }
    )
    { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HabitsList(habits = habits, navigateToHabitDetails)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreenTopAppBar() {
    TopAppBar(
        title = {
            Text(text = "Your habits to achieve")
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

@Composable
fun HabitsList(
    habits: List<Habit>,
    navigateToHabitDetails: (Habit?) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(habits) { habit ->
            HabitCard(habit = habit, navigateToHabitDetails)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitCard(
    habit: Habit,
    navigateToHabitDetails: (Habit?) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = { isExpanded = !isExpanded },
                onLongClick = { navigateToHabitDetails(habit) }
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
fun HabitsFloatingActionButton(navigateToHabitDetails: (Habit?) -> Unit) {
    FloatingActionButton(
        onClick = { navigateToHabitDetails(null) }
    ) {
        Icon(Icons.Filled.Add, "New habit")
    }
}

@Composable
fun HabitLinearProgressBar(habit: Habit, modifier: Modifier) {
    val targetProgress = habit.completeTimes.toFloat() / habit.targetTimes.toFloat()
    Log.d("HabitLinearProgressBar", "targetProgress = $targetProgress")

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
fun PreviewHabitCard() {
    HabitCard(
        habit = Habit(
            id = UUID.randomUUID().toString(),
            name = "fitness",
            description = "go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym",
            priority = HabitPriority.High,
            type = HabitType.Good,
            targetTimes = 3,
            completeTimes = 0,
            period = 7,
            color = Color.Blue.value.toInt()
        ),
        navigateToHabitDetails = { }
    )
}


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
        ),
        navigateToHabitDetails = { }
    )
}

