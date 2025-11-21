package com.enesay.android_task.domain.model

data class UserInfo(
    val active: Boolean?,
    val businessUnit: String?,
    val displayName: String?,
    val firstName: String?,
    val lastName: String?,
    val personalNo: Int?
)