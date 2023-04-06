package com.monnl.habitual.ui.habits

import androidx.lifecycle.ViewModel
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.models.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SingleHabitViewModel : ViewModel() {
    private val _habitState = MutableStateFlow(Habit())
    val habitState: StateFlow<Habit> = _habitState

    var habitId: String? = null
        set(value) {
            field = value
            fetchHabit(value)
        }

    private fun fetchHabit(habitId: String?) {
        val habit = HabitsDataSource.getHabit(habitId)
        habit?.let { _habitState.value = habit.copy() }
    }

    fun updateHabit(habit: Habit) = HabitsDataSource.updateHabit(habit)
}
