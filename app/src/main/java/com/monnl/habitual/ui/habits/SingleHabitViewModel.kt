package com.monnl.habitual.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.monnl.habitual.MyApplication
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.repos.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SingleHabitViewModel(
    private val habitsRepository: HabitsRepository
) : ViewModel() {
    private val _habitState = MutableStateFlow(Habit())
    val habitState: StateFlow<Habit> = _habitState

    var habitId: String? = null
        set(value) {
            field = value
            fetchHabit(value)
        }

    private fun fetchHabit(habitId: String?) {
        if (!habitId.isNullOrBlank()) _habitState.value = habitsRepository.getHabit(habitId)
    }

    fun updateHabit(habit: Habit) {
        habitsRepository.apply {
            if (!habitId.isNullOrBlank()) this.updateHabit(habit) else this.addHabit(habit)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val habitsRepository =
                    (this[APPLICATION_KEY] as MyApplication).appContainer.habitsRepository
                SingleHabitViewModel(habitsRepository)
            }
        }
    }
}
