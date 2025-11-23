package com.enesay.android_task.data

import com.enesay.android_task.utils.PASSWORD
import com.enesay.android_task.utils.USERNAME

data class LoginRequest(
    val userName: String = USERNAME,
    val password: String = PASSWORD
)