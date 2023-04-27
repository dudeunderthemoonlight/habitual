package com.monnl.habitual.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getById(id: String): DatabaseHabit

    @Query("SELECT * FROM habits WHERE id = :id")
    fun observeHabit(id: String?): Flow<DatabaseHabit>

    @Query("SELECT * FROM habits")
    fun getAll(): Flow<List<DatabaseHabit>>

    @Update
    suspend fun update(habit: DatabaseHabit)

    @Insert
    suspend fun insertAll(vararg habits: DatabaseHabit)

    @Delete
    suspend fun delete(habit: DatabaseHabit)
}