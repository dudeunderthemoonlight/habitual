package com.monnl.habitual.ui.habits


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.monnl.habitual.MyApplication
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.priorityMap
import com.monnl.habitual.data.repos.HabitsRepository
import com.monnl.habitual.ui.habits.sorting.SortContentState
import com.monnl.habitual.ui.habits.sorting.SortOptions
import com.monnl.habitual.utils.modifyIf
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HabitsViewModel(
    private val habitsRepository: HabitsRepository
) : ViewModel() {

    private val habitsFlow = habitsRepository.habits
    private val searchFlow = MutableStateFlow("")
    private val sortFlow = MutableStateFlow<SortContentState?>(null)

    val suggestedHabits: Flow<List<Habit>> =
        combine(habitsFlow, searchFlow, sortFlow) { habits, search, sort ->
            habits
                .filter { it.name.contains(search) }
                .modifyIf(sort != null) {
                    this.sortedWith(compareBy { calculateParameter(sort!!.parameter, it) })
                        .modifyIf(sort!!.descending) { reversed() }
                }
        }

    fun toHabitNameChanged(newName: String) {
        searchFlow.value = newName
    }

    fun sortBy(sortState: SortContentState) {
        sortFlow.value = sortState
    }

    fun refreshHabits() = viewModelScope.launch { habitsRepository.refreshHabits() }

    private fun calculateParameter(parameter: SortOptions, habit: Habit): Comparable<*> =
        when (parameter) {
            SortOptions.CompleteStatus -> habit.completeTimes!!.toFloat() / habit.targetTimes!!.toFloat()
            SortOptions.Priority -> priorityMap[habit.priority]!!
        }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val habitsRepository =
                    (this[APPLICATION_KEY] as MyApplication).appContainer.habitsRepository
                HabitsViewModel(habitsRepository)
            }
        }
    }
}
