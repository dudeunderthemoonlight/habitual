package com.monnl.habitual.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.monnl.habitual.data.models.models.Habit
import com.monnl.habitual.data.models.models.HabitType

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
                verticalAlignment = CenterVertically
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
