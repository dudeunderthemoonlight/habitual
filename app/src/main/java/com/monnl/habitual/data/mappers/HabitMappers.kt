package com.monnl.habitual.data.mappers

import com.monnl.habitual.data.models.*
import com.monnl.habitual.data.network.NetworkModel.NetworkHabit
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

fun NetworkHabit.asDatabaseModel(): DatabaseHabit =
    DatabaseHabit(
        id = uid,
        name = title,
        description = description,
        priority = intToPriorityMap[priority]!!,
        type = intToTypeMap[type]!!,
        completeTimes = done_dates.size,
        targetTimes = count,
        period = frequency,
        color = color
    )

fun Habit.asNetworkModel(): NetworkHabit =
    NetworkHabit(
        uid = id,
        title = name,
        description = description,
        color = color,
        count = targetTimes!!,
        date = 0,
        done_dates = buildList(completeTimes!!) { },
        priority = priorityMap[priority]!!,
        frequency = period!!,
        type = typeMap[type]!!
    )