package com.monnl.habitual.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.monnl.habitual.data.models.HabitPriority
import com.monnl.habitual.data.models.HabitType

@Entity(tableName = "habits")
data class DatabaseHabit constructor(
    @PrimaryKey @ColumnInfo("id") val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("priority") val priority: HabitPriority,
    @ColumnInfo("type") val type: HabitType,
    @ColumnInfo("target") val targetTimes: Int,
    @ColumnInfo("complete") val completeTimes: Int,
    @ColumnInfo("period") val period: Int,
    @ColumnInfo("color") val color: Int
)
