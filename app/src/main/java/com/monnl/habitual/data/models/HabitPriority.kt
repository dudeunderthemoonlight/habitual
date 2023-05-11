package com.monnl.habitual.data.models

enum class HabitPriority {
    High, Medium, Low
}

val priorityMap = mapOf(
    HabitPriority.High to 1,
    HabitPriority.Medium to 2,
    HabitPriority.Low to 3
)

val intToPriorityMap = mapOf(
    0 to HabitPriority.High,
    1 to HabitPriority.Medium,
    2 to HabitPriority.Low
)