package com.artem.myapplication.network

import com.google.gson.annotations.SerializedName

data class ScheduleResponse(
    val group: String,
    val days: List<DaySchedule>
)

data class DaySchedule(
    val dayOfWeek: String,
    val classes: List<ClassItem>
)

data class ClassItem(
    @SerializedName("lesson_number") val lessonNumber: Int,
    val subject: String,
    val teacher: String,
    val time: String,
    val room: String
)