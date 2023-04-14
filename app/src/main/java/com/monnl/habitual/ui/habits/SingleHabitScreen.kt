package com.monnl.habitual.ui.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.monnl.habitual.*
import com.monnl.habitual.R
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.ui.components.HabitTypeRadioButton
import com.monnl.habitual.ui.components.SaveHabitButton

@Composable
fun SingleHabitScreen(
    habitId: String?,
    onSaveButtonClick: () -> Unit,
    viewModel: SingleHabitViewModel = viewModel<SingleHabitViewModel>(
        factory = SingleHabitViewModel.Factory
    ).apply {
        this.habitId = habitId
    }
) {
    val habit = viewModel.habitState.collectAsState().value
    Card(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
            .wrapContentSize(Alignment.Center)
            .verticalScroll(rememberScrollState())
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
                //modifier = Modifier.fillMaxWidth()
            )
            HabitTargetPeriodicity(
                habit = habit,
                modifier = Modifier.fillMaxWidth()
            )
            SaveHabitButton(
                habit = habit,
                onButtonClick = onSaveButtonClick,
                viewModel = viewModel
            )
        }
    }
}

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
        label = { Text(stringResource(R.string.name)) }
    )
}

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
        label = { Text(stringResource(R.string.description)) }
    )
}


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

            Text(stringResource(R.string.targetTimes))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                value = targetTimes,
                onValueChange = {
                    if (it.isEmpty() || it.matches(numberPattern)) {
                        habit.targetTimes = it.toIntOrNull()
                        targetTimes = it
                    }
                },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
            )
        }
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.periodicity))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                value = periodicity,
                onValueChange = {
                    if (it.isEmpty() || it.matches(numberPattern)) {
                        habit.period = it.toIntOrNull()
                        periodicity = it
                    }
                },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
            )
            Text(stringResource(R.string.periodicity_days))
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
                label = { Text(stringResource(R.string.priority)) },
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
                        text = { Text(selectionOption.toString()) }
                    )
                }
            }
        }
    }
}
