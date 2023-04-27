package com.monnl.habitual.data.repos

import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.sources.LocalHabitsDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class HabitsRepositoryImpl(
    private val localHabitsDataSource: LocalHabitsDataSource,
    private val externalScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) : HabitsRepository {

    override val habits: Flow<List<Habit>>
        get() = localHabitsDataSource.getHabits()

    override fun observeHabit(id: String?): Flow<Habit> = localHabitsDataSource.observeHabit(id)

    override suspend fun getHabit(id: String): Habit =
        withContext(ioDispatcher) { localHabitsDataSource.getHabit(id) }

    override suspend fun updateHabit(habit: Habit) {
        externalScope.launch { localHabitsDataSource.updateHabit(habit) }
    }

    override suspend fun addHabit(habit: Habit) {
        externalScope.launch { localHabitsDataSource.addHabit(habit) }
    }

    override suspend fun delete(habit: Habit) {
        externalScope.launch { localHabitsDataSource.deleteHabit(habit) }
    }
}
