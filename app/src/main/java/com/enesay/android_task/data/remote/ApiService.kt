package com.enesay.android_task.data.remote

import com.enesay.android_task.domain.model.LoginResponse
import com.enesay.android_task.domain.model.TaskDTO
import com.enesay.android_task.utils.LOGIN_TOKEN
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @GET("v1/tasks/select")
    suspend fun getTasks(): List<TaskDTO>

    @Headers(
        "Authorization: Basic $LOGIN_TOKEN",
        "Content-Type: application/json"
    )
    @POST("/index.php/login")
    suspend fun login(@Body creds: LoginRequest): LoginResponse
}