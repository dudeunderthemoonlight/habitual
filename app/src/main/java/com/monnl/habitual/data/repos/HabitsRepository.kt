package com.monnl.habitual.data.repos

import com.monnl.habitual.data.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {
    val habits: Flow<List<Habit>>
    fun observeHabit(id: String?): Flow<Habit>
    suspend fun getHabit(id: String): Habit
    suspend fun updateHabit(habit: Habit)
    suspend fun addHabit(habit: Habit)
    suspend fun delete(habit: Habit)
}