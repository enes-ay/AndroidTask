package com.enesay.android_task.ui.screens.TaskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.usecase.GetTasksUseCase
import com.enesay.android_task.domain.usecase.RefreshTasksUseCase
import com.enesay.android_task.domain.usecase.SearchTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val refreshTasksUseCase: RefreshTasksUseCase
) : ViewModel() {

    init {
        getTasksUseCase()
        viewModelScope.launch {
            refreshTasksUseCase()
        }
    }

    private val _query = MutableStateFlow("")
    //private val _state = MutableStateFlow<TaskListState>(TaskListState.Empty)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val tasks: StateFlow<List<TaskModel>> = _query.debounce(300) // typing debounce
        .flatMapLatest { q ->
            if (q.isBlank()) getTasksUseCase() else searchTasksUseCase(q)
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setQuery(q: String) {
        _query.value = q
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                refreshTasksUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isRefreshing.value = false
            }
        }
    }
}
