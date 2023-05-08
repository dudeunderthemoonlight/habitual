package com.monnl.habitual.ui.habits

import com.monnl.habitual.data.models.Habit

sealed class HabitState {
    object Loading: HabitState()
    data class Success(val habit: Habit) : HabitState()
    object Error: HabitState()
}