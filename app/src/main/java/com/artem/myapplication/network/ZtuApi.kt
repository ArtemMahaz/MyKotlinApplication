package com.artem.myapplication.network

import retrofit2.http.GET

interface ZtuApi {
    @GET("schedule/current")
    suspend fun getSchedule(): ScheduleResponse
}