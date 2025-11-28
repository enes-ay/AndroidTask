package com.enesay.android_task.domain.model

import com.google.gson.annotations.SerializedName

data class TaskDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("task")
    val task: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("sort")
    val sort: String?,
    @SerializedName("wageType")
    val wageType: String?,
    @SerializedName("businessUnitKey")
    val businessUnitKey: String?,
    @SerializedName("businessUnit")
    val businessUnit: String?,
    @SerializedName("parentTaskID")
    val parentTaskID: String?,
    @SerializedName("preplanningBoardQuickSelect")
    val preplanningBoardQuickSelect: String?,
    @SerializedName("colorCode")
    val colorCode: String?,
    @SerializedName("workingTime")
    val workingTime: String?,
    @SerializedName("isAvailableInTimeTrackingKioskMode")
    val isAvailableInTimeTrackingKioskMode: Boolean?,
    @SerializedName("isAbstract")
    val isAbstract: Boolean?,
    @SerializedName("externalId")
    val externalId: String?
)