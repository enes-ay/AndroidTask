package com.enesay.android_task.data.remote

import com.enesay.android_task.data.LoginRequest
import com.enesay.android_task.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("v1/tasks/select")
    suspend fun getTasks(): List<TaskDTO>

    @POST("/index.php/login")
    suspend fun login(@Body creds: LoginRequest): LoginResponse
}