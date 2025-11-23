package com.enesay.android_task.domain.repository

import com.enesay.android_task.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<TaskModel>>
    fun searchTask(query: String): Flow<List<TaskModel>>
    suspend fun updateTasks()
}