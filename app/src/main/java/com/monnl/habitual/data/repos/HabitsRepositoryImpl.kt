package com.monnl.habitual.data.repos

import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.sources.LocalHabitsDataSource
import kotlinx.coroutines.flow.Flow

class HabitsRepositoryImpl(
    private val localHabitsDataSource: LocalHabitsDataSource
) : HabitsRepository {

    override val habits: Flow<List<Habit>>
        get() = localHabitsDataSource.getHabits()

    override fun getHabit(id: String): Habit = localHabitsDataSource.getHabit(id)
    override fun updateHabit(habit: Habit) = localHabitsDataSource.updateHabit(habit)
    override fun addHabit(habit: Habit) = localHabitsDataSource.addHabit(habit)
    override fun delete(habit: Habit) = localHabitsDataSource.deleteHabit(habit)
}
