package com.monnl.habitual.ui.habits

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monnl.habitual.*
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.models.Habit
import com.monnl.habitual.data.models.models.HabitPriority
import com.monnl.habitual.ui.components.HabitTypeRadioButton
import com.monnl.habitual.ui.components.SaveHabitButton

@Composable
fun SingleHabitScreen(
    habitId: String?,
    onSaveButtonClick: () -> Unit
) {
    val habit by rememberSaveable { mutableStateOf(HabitsDataSource.getHabit(habitId) ?: Habit()) }

    Card(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp),
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
            HabitTargetPeriodicity(
                habit = habit,
                modifier = Modifier.fillMaxWidth()
            )
            SaveHabitButton(
                habit = habit,
                onButtonClick = onSaveButtonClick
            )
        }
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
fun HabitTargetPeriodicity(
    habit: Habit,
    modifier: Modifier = Modifier
) {
    var targetTimes by remember { mutableStateOf(habit.targetTimes?.toString() ?: "") }
    var periodicity by remember { mutableStateOf(habit.period?.toString() ?: "") }

    val numberPattern = remember { Regex("^\\d+\$") }

    Column() {
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
            Text(text = " times, ")
        }
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = " In ")
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
