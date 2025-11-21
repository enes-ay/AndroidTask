package com.enesay.android_task.domain.model

data class TaskItem(
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
