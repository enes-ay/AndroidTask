package com.enesay.android_task.domain.usecase

import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<TaskModel>> = repository.searchTask(query)
}
