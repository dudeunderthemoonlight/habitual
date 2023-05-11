package com.monnl.habitual.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Habit(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var priority: HabitPriority = HabitPriority.Medium,
    var type: HabitType = HabitType.Good,
    var targetTimes: Int? = null,
    var completeTimes: Int? = 0,
    var period: Int? = null,
    val color: Int = 0
) : Parcelable
