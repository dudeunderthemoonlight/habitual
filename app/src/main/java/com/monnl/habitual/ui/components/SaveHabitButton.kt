package com.monnl.habitual.ui.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.models.Habit

@Composable
fun SaveHabitButton(
    habit: Habit,
    onButtonClick: () -> Unit
) {
    val activity = LocalContext.current as Activity
    ElevatedButton(
        onClick = {
            if (isHabitValid(habit)) {
                HabitsDataSource.updateHabit(habit)
                onButtonClick()
            } else Toast.makeText(activity, "fill whole data, please", Toast.LENGTH_LONG).show()
        }

    ) {
        Text(text = "Save habit")
    }
}

fun isHabitValid(habit: Habit): Boolean =
    !(habit.name.isBlank()
            || habit.description.isBlank()
            || habit.period == 0
            || habit.targetTimes == 0)
