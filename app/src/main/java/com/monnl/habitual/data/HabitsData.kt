package com.monnl.habitual.data

import android.graphics.Color
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.data.models.HabitType
import com.monnl.habitual.data.room.DatabaseHabit
import java.util.*

object HabitsData {
    val PREPOPULATE_HABITS = arrayOf(
        DatabaseHabit(
            id = UUID.randomUUID().toString(),
            name = "fitness",
            description = "go to the gym, go to the gym, go to the gym, go to the gym, go to the gym",
            priority = HabitPriority.High,
            type = HabitType.Good,
            targetTimes = 4,
            completeTimes = 2,
            period = 7,
            color = Color.CYAN
        ), DatabaseHabit(
            id = UUID.randomUUID().toString(),
            name = "food",
            description = "eat 3 times a day for a week",
            priority = HabitPriority.Medium,
            type = HabitType.Good,
            targetTimes = 7,
            completeTimes = 2,
            period = 7,
            color = Color.CYAN
        ), DatabaseHabit(
            id = UUID.randomUUID().toString(),
            name = "sleep",
            description = "go to bed earlier than 12 pm",
            priority = HabitPriority.High,
            type = HabitType.Good,
            targetTimes = 7,
            completeTimes = 7,
            period = 7,
            color = Color.CYAN
        ),
        DatabaseHabit(
            id = UUID.randomUUID().toString(),
            name = "smoking",
            description = "STOP it",
            priority = HabitPriority.High,
            type = HabitType.Bad,
            targetTimes = 4,
            completeTimes = 2,
            period = 7,
            color = Color.CYAN
        ),
        DatabaseHabit(
            id = UUID.randomUUID().toString(),
            name = "some name",
            description = "some description",
            priority = HabitPriority.Low,
            type = HabitType.Bad,
            targetTimes = 4,
            completeTimes = 2,
            period = 7,
            color = Color.CYAN
        )
    )
}