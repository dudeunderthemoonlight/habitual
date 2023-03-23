package com.monnl.habitual

import com.monnl.habitual.data.HabitsDataSource
import org.junit.Test

import org.junit.Assert.*


class HabitDataTest {

    private val habitsDataSource = HabitsDataSource
    private val habit = habitsDataSource.habits[0]

    @Test
    fun getHabitIsCorrect() {
        assertEquals(
            habit,
            HabitsDataSource.getHabit(habit.id)
        )
    }

    @Test
    fun isInListIsCorrect() {
        assertEquals(
            true,
            HabitsDataSource.habitInList(habit)
        )
    }
}