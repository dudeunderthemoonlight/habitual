package com.monnl.habitual.data.network

import retrofit2.http.*
import com.monnl.habitual.data.network.NetworkModel.NetworkHabit
import com.monnl.habitual.data.network.NetworkModel.HabitUID

interface HabitService {
    @GET("habit")
    suspend fun getHabits(@Header("Authorization") token: String): List<NetworkHabit>

    @PUT("habit")
    suspend fun addHabit(
        @Header("Authorization") token: String,
        @Body habit: NetworkHabit
    ): NetworkModel

    @POST("habit_done")
    suspend fun doneHabit(
        @Header("Authorization") token: String,
        @Body habitUID: HabitUID
    ): NetworkModel

    @DELETE("habit")
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Body habitUID: NetworkModel
    )
}