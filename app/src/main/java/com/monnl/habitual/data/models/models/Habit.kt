package com.monnl.habitual.data.models.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    var priority: HabitPriority = HabitPriority.Medium,
    var type: HabitType = HabitType.Good,
    var targetTimes: Int? = null,
    val completeTimes: Int? = 0,
    var period: Int? = null,
    val color: Int = 0
) : Parcelable
