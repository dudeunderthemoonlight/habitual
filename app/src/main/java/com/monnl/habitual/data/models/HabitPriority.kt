package com.monnl.habitual.data.models

enum class HabitPriority {
    High, Medium, Low
}

val priorityMap = mapOf(
    HabitPriority.High to 1,
    HabitPriority.Medium to 2,
    HabitPriority.Low to 3
)