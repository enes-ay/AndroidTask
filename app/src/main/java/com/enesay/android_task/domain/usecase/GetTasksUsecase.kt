package com.enesay.android_task.domain.usecase

import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<TaskModel>> = repository.getTasks()
}
