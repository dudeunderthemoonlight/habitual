package com.monnl.habitual

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.data.models.HabitType
import com.monnl.habitual.ui.theme.HabitualTheme
import java.util.*

class HabitActivity : ComponentActivity() {

    private val habitMutableState: MutableState<Habit?> = mutableStateOf(null)

    private lateinit var editingState: EditingState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readExtras()

        Log.d(this.javaClass.simpleName, "habit: ${habitMutableState.value}")

        setContent {
            HabitualTheme {
                HabitScreen(habit = habitMutableState, state = editingState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(HABIT_KEY, habitMutableState.value)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        habitMutableState.value = savedInstanceState.getParcelable(HABIT_KEY) as Habit?
    }

    private fun readExtras() = intent.extras?.run {
        val habit = getParcelable(HABIT_KEY) as Habit?

        editingState =
            if (habit != null) EditingState.EXISTING_HABIT else EditingState.NEW_HABIT
        habitMutableState.value = habit ?: Habit()

        intent.removeExtra(HABIT_KEY)
    }

    companion object {
        const val HABIT_KEY = "habit"
    }
}

enum class EditingState {
    NEW_HABIT,
    EXISTING_HABIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(habit: MutableState<Habit?>, state: EditingState) {
    Scaffold(
        topBar = { HabitScreenTopAppBar(state) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            EditableHabit(habit)
        }
    }
}

@Composable
fun EditableHabit(habit: MutableState<Habit?>) {
    Card(
        modifier = Modifier
            .padding(top = 20.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                NameTextField(
                    habit = habit,
                    modifier = Modifier.width(290.dp)
                )
            }

            item {
                HabitDescriptionField(
                    habit = habit,
                    modifier = Modifier.width(290.dp)
                )
            }

            item {
                PrioritySpinner(
                    habit = habit,
                    modifier = Modifier.width(290.dp)
                )
            }

            item {
                HabitTypeRadioButton(
                    habit = habit
                )
            }

            item {
                HabitTargetPeriodicity(habit = habit)
            }

            item {
                SaveHabitButton(habit = habit)
            }

        }
    }
}

@Composable
fun SaveHabitButton(habit: MutableState<Habit?>) {
    ElevatedButton(
        onClick = {
            HabitsDataSource.updateHabit(habit.value)
            Log.d("HabitActivity", "habit: ${habit.value}")
        }
    ) {
        Text(text = "Save habit")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTextField(
    habit: MutableState<Habit?>,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf(habit.value?.name ?: "") }

    OutlinedTextField(
        modifier = modifier,
        value = name,
        onValueChange = {
            habit.value?.name = it
            name = it
        },
        maxLines = 1,
        label = { Text("Name") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDescriptionField(
    habit: MutableState<Habit?>,
    modifier: Modifier = Modifier
) {
    var description by rememberSaveable { mutableStateOf(habit.value?.description ?: "") }

    OutlinedTextField(
        modifier = modifier,
        value = description,
        onValueChange = {
            habit.value?.description = it
            description = it
        },
        maxLines = 4,
        label = { Text("Description") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySpinner(
    habit: MutableState<Habit?>,
    modifier: Modifier = Modifier
) {
    var selectedOptionText by rememberSaveable {
        mutableStateOf(
            habit.value?.priority?.toString() ?: HabitPriority.Medium.toString()
        )
    }

    var expanded by remember { mutableStateOf(false) }
    val options = HabitPriority.values()

    Row(modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
        ) {

            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                label = { Text("Priority") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption.toString()
                            expanded = false
                            habit.value?.priority = selectionOption
                        },
                        text = { Text(text = selectionOption.toString()) }
                    )
                }
            }
        }
    }
}

@Composable
fun HabitTypeRadioButton(
    habit: MutableState<Habit?>,
    modifier: Modifier = Modifier
) {
    var habitType by rememberSaveable { mutableStateOf(habit.value?.type ?: HabitType.Good) }

    val types = HabitType.values()
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(habitType) }

    Row(
        modifier
            .selectableGroup()
    ) {
        Text(text = "Choose habit type: ")
        types.forEach { type ->
            Row(
                Modifier
                    .weight(1.0f, fill = false)
                    .selectable(
                        selected = (type == selectedOption),
                        onClick = {
                            onOptionSelected(type)
                            habitType = type
                            habit.value?.type = type
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (type == selectedOption),
                    onClick = null
                )
                Text(text = type.toString(), fontSize = 22.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTargetPeriodicity(
    habit: MutableState<Habit?>,
    modifier: Modifier = Modifier
) {
    var completeTimes by rememberSaveable {
        mutableStateOf(
            habit.value?.completeTimes?.toString() ?: ""
        )
    }
    var targetTimes by rememberSaveable {
        mutableStateOf(
            habit.value?.targetTimes?.toString() ?: ""
        )
    }
    var periodicity by rememberSaveable { mutableStateOf(habit.value?.period?.toString() ?: "") }

    val numberPattern = remember { Regex("^\\d+\$") }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "I'm gonna do it ")

        OutlinedTextField(
            modifier = Modifier.width(40.dp),
            maxLines = 1,
            value = targetTimes,
            onValueChange = {
                if (it.matches(numberPattern)) {
                    habit.value?.targetTimes = it.toInt()
                    targetTimes = it
                }
            })

        Text(text = " times in ")

        OutlinedTextField(
            modifier = Modifier.width(40.dp),
            maxLines = 1,
            value = periodicity,
            onValueChange = {
                if (it.matches(numberPattern)) {
                    habit.value?.period = it.toInt()
                    periodicity = it
                }
            })

        Text(text = " days.")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreenTopAppBar(state: EditingState) {
    val topAppBarTitle = if (state == EditingState.NEW_HABIT) "New habit" else "Edit habit"
    TopAppBar(
        title = { Text(topAppBarTitle) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewHabitScreen() {
    val habit = Habit(
        id = UUID.randomUUID().toString(),
        name = "fitness",
        description = "go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym, go to the gym",
        priority = HabitPriority.High,
        type = HabitType.Good,
        targetTimes = 3,
        completeTimes = 0,
        period = 7,
        color = Color.Blue.value.toInt()
    )

    val habitState: MutableState<Habit?> = mutableStateOf(habit)

    HabitScreen(habit = habitState, state = EditingState.EXISTING_HABIT)
}