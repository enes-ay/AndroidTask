package com.enesay.android_task.data.remote

data class TaskDTO(
    val id: String,
    val task: String?,
    val title: String?,
    val description: String?,
    val sort: String?,
    val wageType: String?,
    val businessUnitKey: String?,
    val businessUnit: String?,
    val parentTaskID: String?,
    val preplanningBoardQuickSelect: String?,
    val colorCode: String?,
    val workingTime: String?,
    val isAvailableInTimeTrackingKioskMode: Boolean?,
    val isAbstract: Boolean?,
    val externalId: String?
)