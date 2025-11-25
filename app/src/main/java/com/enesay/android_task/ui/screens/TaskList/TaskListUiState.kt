package com.enesay.android_task.ui.screens.TaskList

import com.enesay.android_task.domain.model.TaskModel
data class TaskListUiState(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val contentState: TaskContentState = TaskContentState.Loading,
    val isRefreshing: Boolean = false,
)

sealed interface TaskContentState {
    data object Loading : TaskContentState
    data class Success(val tasks: List<TaskModel>) : TaskContentState
    data object Empty : TaskContentState
    data class Error(val message: String) : TaskContentState
}