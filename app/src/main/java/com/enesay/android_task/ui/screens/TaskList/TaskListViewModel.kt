package com.enesay.android_task.ui.screens.TaskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enesay.android_task.di.IoDispatcher
import com.enesay.android_task.domain.usecase.GetTasksUseCase
import com.enesay.android_task.domain.usecase.RefreshTasksUseCase
import com.enesay.android_task.domain.usecase.SearchTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val refreshTasksUseCase: RefreshTasksUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)
    private val _effect = Channel<TaskListEffect>()
    val effect = _effect.receiveAsFlow()
    private val _isFirstLoad = MutableStateFlow(true)
    private val _isRefreshing = MutableStateFlow(false)


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _contentStateFlow: Flow<TaskContentState> = _searchQuery
        .map {
            it.trim()
        }
        .debounce(300)
        .filter{ query ->
            query.isBlank() || query.length >= 2
        }
        .flatMapLatest { query ->
            // Deciding which data flow will be used
            val sourceFlow = if (query.isBlank()) getTasksUseCase() else searchTasksUseCase(query)

            combine(sourceFlow, _isFirstLoad) { tasks, isFirstLoad ->

                if (tasks.isNotEmpty()) {
                    TaskContentState.Success(tasks)
                } else {
                    if (isFirstLoad) {
                        TaskContentState.Loading
                    } else {
                        TaskContentState.Empty
                    }
                }
            }
        }
        .flowOn(ioDispatcher)
        // Error handling when get an error
        .catch { emit(TaskContentState.Error(it.message ?: "Error")) }

    val uiState: StateFlow<TaskListUiState> = combine(
        _searchQuery,
        _isSearchActive,
        _contentStateFlow,
        _isRefreshing
    ) { query, isActive, content, refreshing ->
        TaskListUiState(
            searchQuery = query,
            isSearchActive = isActive,
            contentState = content,
            isRefreshing = refreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TaskListUiState()
    )


    fun onEvent(event: TaskListUiEvent) {
        when (event) {
            is TaskListUiEvent.OnSearchQueryChange -> {
                _searchQuery.value = event.query
            }

            is TaskListUiEvent.OnSearchActiveChange -> {
                _isSearchActive.value = event.isActive
                if (!event.isActive) _searchQuery.value = ""
            }

            is TaskListUiEvent.OnRefresh -> {
                refreshData()
            }
        }
    }

    init {
        refreshData(isInitial = true)
    }

    // Helper Function for data update
    private fun refreshData(isInitial: Boolean = false) {
        viewModelScope.launch(ioDispatcher) {
            // Pull-2-refresh triggers its own spinner
            if (!isInitial) _isRefreshing.value = true

            try {
                refreshTasksUseCase()
            } catch (e: Exception) {
                // One-time Toast message with Channel mechanism
                val errorMessage =
                    if (e is java.io.IOException) "No Internet Connection" else "${e.message}"
                _effect.send(TaskListEffect.ShowToast(errorMessage))
                e.printStackTrace()
            } finally {
                delay(200)
                _isRefreshing.value = false
                _isFirstLoad.value = false
            }
        }
    }
}