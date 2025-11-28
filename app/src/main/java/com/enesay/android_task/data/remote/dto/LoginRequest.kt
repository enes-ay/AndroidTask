package com.enesay.android_task.data.remote.dto

import com.enesay.android_task.utils.PASSWORD
import com.enesay.android_task.utils.USERNAME
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    val username: String = USERNAME,
    @SerializedName("password")
    val password: String = PASSWORD
)