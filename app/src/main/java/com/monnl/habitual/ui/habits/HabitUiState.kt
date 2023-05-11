package com.monnl.habitual.ui.habits

import com.monnl.habitual.data.models.Habit

sealed class HabitUiState {
    object Loading: HabitUiState()
    data class Success(val habit: Habit) : HabitUiState()
    object Error: HabitUiState()
}