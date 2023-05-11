package com.monnl.habitual.data.sources

import android.content.Context
import com.monnl.habitual.R
import com.monnl.habitual.data.mappers.asDatabaseModel
import com.monnl.habitual.data.mappers.asNetworkModel
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.network.HabitService
import com.monnl.habitual.data.network.NetworkModel
import com.monnl.habitual.data.room.DatabaseHabit

class RemoteHabitsDataSource(
    private val service: HabitService,
    context: Context
) {
    private val token: String = context.getString(R.string.auth_token)
    suspend fun getDatabaseHabits(): List<DatabaseHabit> =
        service.getHabits(token).map { it.asDatabaseModel() }

    suspend fun addHabit(habit: Habit): NetworkModel =
        service.addHabit(token, habit.asNetworkModel())

    suspend fun deleteHabit(id: String) = service.deleteHabit(token, NetworkModel.HabitUID(id))
    suspend fun doneHabit(id: String): NetworkModel =
        service.doneHabit(token, NetworkModel.HabitUID(id))
}