package com.monnl.habitual.ui.habits


import androidx.lifecycle.ViewModel
import com.monnl.habitual.data.HabitsDataSource
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.priorityMap
import com.monnl.habitual.ui.habits.sorting.SortContentState
import com.monnl.habitual.ui.habits.sorting.SortOptions
import com.monnl.habitual.utils.modifyIf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class HabitsViewModel : ViewModel() {

    private val habitsFlow = HabitsDataSource.habitsFlow
    private val searchFlow = MutableStateFlow("")
    private val sortFlow = MutableStateFlow<SortContentState?>(null)

    val suggestedHabits: StateFlow<List<Habit>> =
        combine(habitsFlow, searchFlow, sortFlow) { habits, search, sort ->
            habits
                .filter { it.name.contains(search) }
                .modifyIf(sort != null) {
                    this.sortedWith(compareBy { calculateParameter(sort!!.parameter, it) })
                        .modifyIf(sort!!.descending) { reversed() }
                }
        }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.WhileSubscribed(), emptyList())

    fun toHabitNameChanged(newName: String) {
        searchFlow.value = newName
    }

    fun sortBy(sortState: SortContentState) {
        sortFlow.value = sortState
    }

    private fun calculateParameter(parameter: SortOptions, habit: Habit): Comparable<*> =
        when (parameter) {
            SortOptions.CompleteStatus -> habit.completeTimes!!.toFloat() / habit.targetTimes!!.toFloat()
            SortOptions.Priority -> priorityMap[habit.priority]!!
        }
}
