package com.enesay.android_task.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class TaskModel(
    val taskId : String,
    val task : String,
    val title : String,
    val description : String,
    val colorCode : String
)