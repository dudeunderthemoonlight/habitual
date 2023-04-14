package com.monnl.habitual.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits WHERE id = :id")
    fun getById(id: String): DatabaseHabit

    @Query("SELECT * FROM habits")
    fun getAll(): Flow<List<DatabaseHabit>>

    @Update
    fun update(habit: DatabaseHabit)

    @Insert
    fun insertAll(vararg habits: DatabaseHabit)

    @Delete
    fun delete(habit: DatabaseHabit)
}