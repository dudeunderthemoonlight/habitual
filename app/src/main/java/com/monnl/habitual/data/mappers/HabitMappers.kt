package com.monnl.habitual.data.mappers

import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.room.DatabaseHabit

fun Habit.asDatabaseModel(): DatabaseHabit =
    DatabaseHabit(
        id,
        name,
        description,
        priority,
        type,
        targetTimes!!,
        completeTimes!!,
        period!!,
        color
    )

fun DatabaseHabit.asDomainModel(): Habit =
    Habit(id, name, description, priority, type, targetTimes, completeTimes, period, color)

