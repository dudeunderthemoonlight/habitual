package com.monnl.habitual.ui.habits

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.monnl.habitual.R
import com.monnl.habitual.ui.habits.sorting.SortContentState
import com.monnl.habitual.ui.habits.sorting.SortOptions


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsBottomSheetScaffold(
    content: @Composable (PaddingValues) -> Unit,
    viewModel: HabitsViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetContent = {
            Column(
                Modifier.padding(top = 20.dp, start = 53.dp, end = 53.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                BottomSheetSearchContent(onToHabitNameChanged = { viewModel.toHabitNameChanged(it) })
                BottomSheetSortContent(onSortButtonPressed = { viewModel.sortBy(it) })
            }
        },
        scaffoldState = scaffoldState,
        content = { innerPadding ->
            content(innerPadding)
        }
    )
}

@Composable
fun BottomSheetSearchContent(
    onToHabitNameChanged: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToHabitNameUserInput(
            hint = stringResource(id = R.string.enter_name),
            label = stringResource(id = R.string.search),
            onInputChanged = onToHabitNameChanged
        )
    }
}

@Composable
fun BottomSheetSortContent(
    onSortButtonPressed: (SortContentState) -> Unit
) {
    var selectedOption = SortOptions.Priority
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        BottomSheetSortingSpinner(
            modifier = Modifier.weight(0.5f),
            startOption = selectedOption,
            onSortOptionChanged = { selectedOption = it })
        BottomSheetSortingButtonRow(
            modifier = Modifier.weight(0.5f),
            onSortButtonPressed = { onSortButtonPressed(SortContentState(selectedOption, it)) }
        )
    }
}

@Composable
fun BottomSheetSortingButtonRow(
    modifier: Modifier,
    onSortButtonPressed: (Boolean) -> Unit
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(onClick = { onSortButtonPressed(true) }) {
            Text(text = "-")
        }
        Button(onClick = { onSortButtonPressed(false) }) {
            Text(text = "+")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSortingSpinner(
    modifier: Modifier,
    startOption: SortOptions,
    onSortOptionChanged: (SortOptions) -> Unit
) {
    var selectedOption by remember { mutableStateOf(startOption) }
    var expanded by remember { mutableStateOf(false) }

    val options = SortOptions.values()

    Row(modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
        ) {

            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = when (selectedOption) {
                    SortOptions.CompleteStatus -> stringResource(id = R.string.complete_status)
                    SortOptions.Priority -> stringResource(id = R.string.priority_sort)
                },
                onValueChange = { },
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
                            selectedOption = selectionOption
                            expanded = false
                            onSortOptionChanged(selectionOption)
                        },
                        text = {
                            Text(
                                when (selectionOption) {
                                    SortOptions.CompleteStatus -> stringResource(id = R.string.complete_status)
                                    SortOptions.Priority -> stringResource(id = R.string.priority_sort)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ToHabitNameUserInput(
    hint: String,
    label: String,
    onInputChanged: (String) -> Unit
) {
    var textFieldState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue()
        )
    }

    OutlinedTextField(
        value = textFieldState,
        onValueChange = {
            textFieldState = it
            onInputChanged(it.text)
        },
        label = { Text(text = label) },
        placeholder = { Text(text = hint) },
        singleLine = true
    )
}

//@Preview
//@Composable
//fun BottomSheetSortOptionsPreview() {
//    BottomSheetSortingSpinner(onSortOptionChanged = { })
//}