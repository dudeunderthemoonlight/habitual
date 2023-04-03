package com.monnl.habitual.ui.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.monnl.habitual.R
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
            } else Toast.makeText(
                activity,
                activity.getString(R.string.edit_warning),
                Toast.LENGTH_LONG
            ).show()
        }

    ) {
        Text(stringResource(id = R.string.saving_button))
    }
}

fun isHabitValid(habit: Habit): Boolean =
    !(habit.name.isBlank()
            || habit.description.isBlank()
            || habit.period == 0
            || habit.targetTimes == 0)
