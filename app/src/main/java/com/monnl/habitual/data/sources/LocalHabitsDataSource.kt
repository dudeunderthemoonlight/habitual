package com.monnl.habitual.data.sources

import com.monnl.habitual.data.mappers.asDatabaseModel
import com.monnl.habitual.data.mappers.asDomainModel
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.room.HabitsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalHabitsDataSource(private val habits: HabitsDao) {
    fun getHabits(): Flow<List<Habit>> =
        habits.getAll().map { list -> list.map { it.asDomainModel() } }

    fun getHabit(id: String): Habit = habits.getById(id).asDomainModel()
    fun addHabit(habit: Habit) = habits.insertAll(habit.asDatabaseModel())
    fun updateHabit(habit: Habit) = habits.update(habit.asDatabaseModel())
    fun deleteHabit(habit: Habit) = habits.delete(habit.asDatabaseModel())
}