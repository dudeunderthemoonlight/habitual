package com.monnl.habitual.data.repos

import android.util.Log
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.network.NetworkModel.NetworkHabit
import com.monnl.habitual.data.network.NetworkModel.Error
import com.monnl.habitual.data.sources.LocalHabitsDataSource
import com.monnl.habitual.data.sources.RemoteHabitsDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class HabitsRepositoryImpl(
    private val remoteHabitsDataSource: RemoteHabitsDataSource,
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
        externalScope.launch {
            localHabitsDataSource.updateHabit(habit)
            remoteHabitsDataSource.addHabit(habit)
        }
    }

    override suspend fun addHabit(habit: Habit) {
        externalScope.launch {
            when (val response = remoteHabitsDataSource.addHabit(habit)) {
                is NetworkHabit -> localHabitsDataSource.addHabit(habit.copy(id = response.uid))
                is Error -> Log.d(this.javaClass.simpleName, response.message)
                else -> throw IllegalStateException()
            }
        }
    }

    override suspend fun delete(habit: Habit) {
        externalScope.launch {
            localHabitsDataSource.deleteHabit(habit)
            remoteHabitsDataSource.deleteHabit(habit.id)
        }
    }

    override suspend fun refreshHabits() {
        externalScope.launch {
            val habits = remoteHabitsDataSource.getDatabaseHabits()
            localHabitsDataSource.insertDatabaseHabits(habits)
        }
    }
}
