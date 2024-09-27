package com.example.programingdemo.chatApplication.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {
    @POST("/v1/projects/demoprojects-c7203/messages:send")
    @Headers("Content-Type: application/json", "Accept: application/json")

    fun notification(
        @Body message: Notification,
        @Header("Authorization") token: String
    ): Call<Notification>
}
