package com.monnl.habitual.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.monnl.habitual.data.models.models.Habit
import com.monnl.habitual.data.models.models.HabitType
import com.monnl.habitual.R

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
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.radio_button_lead_text)
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                types.forEach { type ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (type == selectedOption),
                                onClick = {
                                    onOptionSelected(type)
                                    habitType = type
                                    habit.type = type
                                }
                            )
                    ) {
                        RadioButton(
                            selected = (type == selectedOption),
                            onClick = null
                        )
                        Text(text = type.toString(), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
