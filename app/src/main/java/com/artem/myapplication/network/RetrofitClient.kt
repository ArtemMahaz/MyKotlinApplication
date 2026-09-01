package com.artem.myapplication.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val mockInterceptor = Interceptor { chain ->
        val jsonResponse = """
        {
          "group": "ІПЗ-22-1",
          "days": [
            {
              "dayOfWeek": "Понеділок",
              "classes": [
                {"lesson_number": 1, "subject": "Бази даних", "teacher": "Іванов І.І.", "time": "08:30 - 09:50", "room": "А-301"},
                {"lesson_number": 2, "subject": "Мобільна розробка", "teacher": "Петров П.П.", "time": "10:00 - 11:20", "room": "А-302"}
              ]
            },
            {
              "dayOfWeek": "Вівторок",
              "classes": [
                {"lesson_number": 1, "subject": "Архітектура ПЗ", "teacher": "Сидоров С.С.", "time": "08:30 - 09:50", "room": "Б-205"},
                {"lesson_number": 3, "subject": "Філософія", "teacher": "Коваленко О.В.", "time": "11:40 - 13:00", "room": "В-410"}
              ]
            }
          ]
        }
        """.trimIndent()

        Thread.sleep(1500)

        Response.Builder()
            .code(200)
            .message("OK")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .body(jsonResponse.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(mockInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.ztu.edu.ua/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ZtuApi = retrofit.create(ZtuApi::class.java)
}