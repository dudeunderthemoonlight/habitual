package com.monnl.habitual

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

    private var currentHabit: Habit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) readExtras()

        setContent {
            HabitualTheme {
                val editingState by rememberSaveable {
                    mutableStateOf(if (currentHabit != null) EditingState.EXISTING_HABIT else EditingState.NEW_HABIT)
                }
                val habit by rememberSaveable { mutableStateOf(currentHabit ?: Habit()) }

                HabitScreen(habit = habit, state = editingState)
            }
        }
    }

    private fun readExtras() = intent.extras?.run {
        currentHabit = getParcelableHabit(this)
    }

    @Suppress("DEPRECATION")
    private fun getParcelableHabit(bundle: Bundle): Habit? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(HABIT_KEY, Habit::class.java)
        } else bundle.getParcelable(HABIT_KEY)
    
    companion object {
        const val HABIT_KEY = "habit"
    }
}

enum class EditingState {
    NEW_HABIT,
    EXISTING_HABIT
}

fun isHabitValid(habit: Habit): Boolean =
    !(habit.name.isBlank()
            || habit.description.isBlank()
            || habit.period == 0
            || habit.targetTimes == 0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(habit: Habit, state: EditingState) {
    Scaffold(
        topBar = { HabitScreenTopAppBar(state) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            EditableHabit(habit)
        }
    }
}

@Composable
fun EditableHabit(habit: Habit) {
    Card(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            NameTextField(
                habit = habit,
                modifier = Modifier.fillMaxWidth()
            )
            HabitDescriptionField(
                habit = habit,
                modifier = Modifier.fillMaxWidth()
            )
            PrioritySpinner(
                habit = habit,
                modifier = Modifier.fillMaxWidth()
            )
            HabitTypeRadioButton(
                habit = habit,
                modifier = Modifier.fillMaxWidth()
            )
            HabitTargetPeriodicity(habit = habit)
            SaveHabitButton(habit = habit)
        }
    }
}

@Composable
fun SaveHabitButton(habit: Habit) {
    val activity = LocalContext.current as Activity
    ElevatedButton(
        onClick = {
            if (isHabitValid(habit)) {
                HabitsDataSource.updateHabit(habit)
                activity.finish()
            } else Toast.makeText(activity, "fill whole data, please", Toast.LENGTH_LONG).show()
            Log.d("HabitActivity", "habit: $habit")
        }

    ) {
        Text(text = "Save habit")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTextField(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(habit.name) }

    OutlinedTextField(
        modifier = modifier,
        value = name,
        onValueChange = {
            habit.name = it
            name = it
        },
        maxLines = 1,
        label = { Text("Name") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDescriptionField(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    var description by remember { mutableStateOf(habit.description) }

    OutlinedTextField(
        modifier = modifier,
        value = description,
        onValueChange = {
            habit.description = it
            description = it
        },
        maxLines = 4,
        label = { Text("Description") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySpinner(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    var selectedOptionText by remember { mutableStateOf(habit.priority.toString()) }
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
                            habit.priority = selectionOption
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
    habit: Habit,
    modifier: Modifier = Modifier
) {
    var habitType by remember { mutableStateOf(habit.type) }
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(habitType) }

    val types = HabitType.values()

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
                            habit.type = type
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
    habit: Habit,
    modifier: Modifier = Modifier
) {
    var targetTimes by remember { mutableStateOf(habit.targetTimes?.toString() ?: "") }
    var periodicity by remember { mutableStateOf(habit.period?.toString() ?: "") }

    val numberPattern = remember { Regex("^\\d+\$") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = "I'm gonna do it ")

        OutlinedTextField(
            modifier = Modifier.width(40.dp),
            maxLines = 1,
            value = targetTimes,
            onValueChange = {
                if (it.isEmpty() || it.matches(numberPattern)) {
                    habit.targetTimes = it.toIntOrNull()
                    targetTimes = it
                }
            })

        Text(text = " times in ")

        OutlinedTextField(
            modifier = Modifier.width(40.dp),
            maxLines = 1,
            value = periodicity,
            onValueChange = {
                if (it.isEmpty() || it.matches(numberPattern)) {
                    habit.period = it.toIntOrNull()
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
    HabitScreen(habit = habit, state = EditingState.EXISTING_HABIT)
}
