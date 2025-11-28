package com.enesay.android_task.data.mapper

import com.enesay.android_task.data.local.TaskEntity
import com.enesay.android_task.data.remote.dto.Task
import com.enesay.android_task.domain.model.TaskModel


fun Task.toEntity(): TaskEntity {

    val safeTitle = this.title ?: "unknown"
    val safeTask = this.task ?: ""
    val safeDesc = this.description ?: ""
    val deterministicId = "${safeTask}_${safeTitle}_${safeDesc}".hashCode().toString()

    return TaskEntity(
        id = deterministicId,
        task = safeTask,
        title = safeTitle,
        description = safeDesc,
        sort = this.sort ?: "0",
        wageType = this.wageType ?: "",
        businessUnitKey = this.businessUnitKey ?: "",
        businessUnit = this.businessUnit ?: "",
        parentTaskID = this.parentTaskID ?: "",
        preplanningBoardQuickSelect = this.preplanningBoardQuickSelect ?: "",
        colorCode = this.colorCode ?: "#FFFFFF",
        workingTime = this.workingTime ?: "",
        isAvailableInTimeTrackingKioskMode = this.isAvailableInTimeTrackingKioskMode ?: false,
        isAbstract = this.isAbstract ?: false,
        externalId = this.externalId ?: ""
    )
}

fun TaskEntity.toDomain(): TaskModel {
    return TaskModel(
        taskId = this.id,
        task = this.task ?: "",
        title = this.title ?: "",
        description = this.description ?: "",
        colorCode = this.colorCode ?: "#CCCCCC"
    )
}