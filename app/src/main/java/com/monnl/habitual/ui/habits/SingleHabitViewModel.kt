package com.monnl.habitual.ui.habits

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.monnl.habitual.MyApplication
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.repos.HabitsRepository
import com.monnl.habitual.ui.navigation.SingleHabit
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SingleHabitViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val habitsRepository: HabitsRepository
) : ViewModel() {

    private val habitId: String? = savedStateHandle[SingleHabit.habitKey]
    private val _habitState = MutableStateFlow<HabitState>(HabitState.Loading)
    val habitState: StateFlow<HabitState> = _habitState

    init { habitId?.let { fetchHabit(it) } }

    private fun fetchHabit(id: String?) {
        if (!id.isNullOrBlank())
            viewModelScope.launch {
                _habitState.value = HabitState.Success(habitsRepository.getHabit(id))
            }
    }

    fun updateHabit(habit: Habit) {
        habitsRepository.let {
            viewModelScope.launch {
                if (!habitId.isNullOrBlank()) it.updateHabit(habit) else it.addHabit(habit)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val habitsRepository =
                    (this[APPLICATION_KEY] as MyApplication).appContainer.habitsRepository
                SingleHabitViewModel(
                    savedStateHandle = savedStateHandle,
                    habitsRepository = habitsRepository
                )
            }
        }
    }
}
