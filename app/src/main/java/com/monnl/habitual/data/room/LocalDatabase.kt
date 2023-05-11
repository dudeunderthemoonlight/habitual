package com.monnl.habitual.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseHabit::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract val habitsDao: HabitsDao

    companion object {
        @Volatile
        private var instance: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): LocalDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "habits_database"
            ).build()
    }
}