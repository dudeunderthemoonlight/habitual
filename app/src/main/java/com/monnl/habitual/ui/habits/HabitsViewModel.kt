package com.monnl.habitual.ui.habits


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.models.Habit
import com.monnl.habitual.data.models.models.priorityMap
import com.monnl.habitual.ui.habits.sorting.SortContentState
import com.monnl.habitual.ui.habits.sorting.SortOptions

class HabitsViewModel : ViewModel() {

    private val _suggestedHabits = MutableLiveData<List<Habit>>()

    val suggestedHabits: LiveData<List<Habit>> get() = _suggestedHabits

    init {
        _suggestedHabits.value = HabitsDataSource.habits
    }

    fun toHabitNameChanged(newName: String) {
        val newHabits = HabitsDataSource.habits
            .filter { it.name.contains(newName) }
        _suggestedHabits.value = newHabits
    }

    /**
     * Sort habits list using [sortState]
     */
    fun sortBy(sortState: SortContentState) {
        val newHabitsOrder = HabitsDataSource.habits.sortedWith(
            compareBy { calculateParameter(sortState.parameter, it) }
        )
        _suggestedHabits.value =
            if (sortState.descending) newHabitsOrder.reversed() else newHabitsOrder
    }

    private fun calculateParameter(parameter: SortOptions, habit: Habit): Comparable<*> =
        when (parameter) {
            SortOptions.CompleteStatus -> habit.completeTimes!!.toFloat() / habit.targetTimes!!.toFloat()
            SortOptions.Priority -> priorityMap[habit.priority]!!
        }

}