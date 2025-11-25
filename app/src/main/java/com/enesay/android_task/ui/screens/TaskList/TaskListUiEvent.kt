package com.enesay.android_task.ui.screens.TaskList

sealed interface TaskListUiEvent {
    data class OnSearchQueryChange(val query: String) : TaskListUiEvent
    data class OnSearchActiveChange(val isActive: Boolean) : TaskListUiEvent
    data object OnRefresh : TaskListUiEvent
}

sealed interface TaskListEffect {
    data class ShowToast(val message: String) : TaskListEffect
}