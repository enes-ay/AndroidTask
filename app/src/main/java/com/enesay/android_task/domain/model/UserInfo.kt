package com.enesay.android_task.domain.model

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("active")
    val active: Boolean?,
    @SerializedName("businessUnit")
    val businessUnit: String?,
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("personalNo")
    val personalNo: Int?
)