package com.monnl.habitual.data.network

import java.io.Serializable

sealed class NetworkModel {
    data class NetworkHabit(
        val color: Int,
        val count: Int,
        val date: Int,
        val description: String,
        val done_dates: List<Int>,
        val frequency: Int,
        val priority: Int,
        val title: String,
        val type: Int,
        val uid: String
    ) : Serializable, NetworkModel()

    data class HabitUID(
        val uid: String
    ) : Serializable, NetworkModel()

    data class HabitDone(
        val date: Int,
        val habit_uid: String
    ) : Serializable, NetworkModel()

    data class Error(
        val code: Int,
        val message: String
    ) : Serializable, NetworkModel()
}

