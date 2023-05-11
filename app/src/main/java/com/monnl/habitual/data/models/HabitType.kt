package com.monnl.habitual.data.models

enum class HabitType {
    Good, Bad
}

val typeMap = mapOf(
    HabitType.Bad to 0,
    HabitType.Good to 1
)

val intToTypeMap = mapOf(
    0 to HabitType.Bad,
    1 to HabitType.Good
)
