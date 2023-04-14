package com.monnl.habitual.utils

import android.content.Context
import com.monnl.habitual.data.repos.HabitsRepositoryImpl
import com.monnl.habitual.data.room.LocalDatabase
import com.monnl.habitual.data.sources.LocalHabitsDataSource

class AppContainer(context: Context) {
    val habitsRepository =
        HabitsRepositoryImpl(LocalHabitsDataSource(LocalDatabase.getDatabase(context).habitsDao))
}