package com.monnl.habitual.data.repos

import com.monnl.habitual.data.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {
    val habits: Flow<List<Habit>>
    fun getHabit(id: String): Habit
    fun updateHabit(habit: Habit)
    fun addHabit(habit: Habit)
    fun delete(habit: Habit)
}