package com.monnl.habitual.data

import android.graphics.Color
import com.monnl.habitual.data.models.Habit
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.data.models.HabitType
import kotlinx.coroutines.flow.*
import java.util.*

object HabitsDataSource {

    private val rawHabitsData = listOf(
        Habit(
            id = UUID.randomUUID().toString(),
            name = "fitness",
            description = "go to the gym, go to the gym, go to the gym, go to the gym, go to the gym",
            priority = HabitPriority.High,
            type = HabitType.Good,
            targetTimes = 4,
            completeTimes = 2,
            period = 7,
            color = Color.CYAN
        ), Habit(
            id = UUID.randomUUID().toString(),
            name = "food",
            description = "eat 3 times a day for a week",
            priority = HabitPriority.Medium,
            type = HabitType.Good,
            targetTimes = 7,
            completeTimes = 2,
            period = 7,
            color = Color.CYAN
        ), Habit(
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
        Habit(
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
        Habit(
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

    private var _habits = listOf<Habit>()
        set(value) {
            _habitsFlow.value = value
            field = value
        }

    private val _habitsFlow = MutableStateFlow(listOf<Habit>())
    val habitsFlow: StateFlow<List<Habit>> = _habitsFlow

    init {
            _habits = rawHabitsData
    }

    fun updateHabit(newHabit: Habit?) {
        if (habitInList(newHabit)) {
            _habits = _habits.map { if (it.id == newHabit?.id) newHabit else it }
        } else {
            addHabit(newHabit)
        }
    }


    fun habitInList(habit: Habit?): Boolean = _habits.firstOrNull { habit?.id == it.id } != null

    private fun addHabit(habit: Habit?) = habit?.let { _habits = _habits + habit }

    fun getHabit(id: String?): Habit? = _habits.firstOrNull { it.id == id }
}