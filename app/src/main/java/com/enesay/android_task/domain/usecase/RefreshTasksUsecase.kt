package com.enesay.android_task.domain.usecase

import com.enesay.android_task.domain.repository.TaskRepository
import javax.inject.Inject

class RefreshTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() = repository.updateTasks()

}