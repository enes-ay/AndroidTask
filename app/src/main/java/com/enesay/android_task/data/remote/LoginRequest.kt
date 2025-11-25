package com.enesay.android_task.data.remote

import com.enesay.android_task.utils.PASSWORD
import com.enesay.android_task.utils.USERNAME

data class LoginRequest(
    val username: String = USERNAME,
    val password: String = PASSWORD
)