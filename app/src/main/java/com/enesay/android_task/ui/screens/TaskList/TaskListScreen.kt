package com.enesay.android_task.ui.screens.TaskList

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.ui.components.DefaultTopBar
import com.enesay.android_task.ui.components.SearchTopBar
import com.enesay.android_task.ui.components.TaskRowItem
import com.enesay.android_task.utils.startQrCodeScanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.contentState) {
        if (state.contentState is TaskContentState.Error) {
            Toast.makeText(
                context,
                (state.contentState as TaskContentState.Error).message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    // One-Shot event
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TaskListEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    TaskListContent(
        state = state,
        onEvent = viewModel::onEvent,
        paddingValues = innerPadding,
        context = context
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListContent(
    state: TaskListUiState,
    onEvent: (TaskListUiEvent) -> Unit,
    paddingValues: PaddingValues,
    context: Context
) {

    val launchQrScanner = {
        startQrCodeScanner(
            context = context,
            onResult = { code ->
                onEvent(TaskListUiEvent.OnSearchActiveChange(true))
                onEvent(TaskListUiEvent.OnSearchQueryChange(code))
            },
            onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    Scaffold(
        // Deciding which topbar will be used
        topBar = {
            if (state.isSearchActive) {
                SearchTopBar(
                    text = state.searchQuery,
                    onTextChange = { onEvent(TaskListUiEvent.OnSearchQueryChange(it)) },
                    onCloseClicked = { onEvent(TaskListUiEvent.OnSearchActiveChange(false)) },
                    onQrClicked = { launchQrScanner() }
                )
            } else {
                DefaultTopBar(
                    onSearchClicked = { onEvent(TaskListUiEvent.OnSearchActiveChange(true)) },
                    onQrClicked = { launchQrScanner() }
                )
            }
        }
    ) { scaffoldPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(scaffoldPadding)
        ) {
            when (val content = state.contentState) {
                // Data Loading
                is TaskContentState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // Empty View
                is TaskContentState.Empty -> {
                    EmptyView(modifier = Modifier.align(Alignment.Center))
                }

                // Error Retry Button
                is TaskContentState.Error -> {
                    ErrorView(
                        message = content.message,
                        onRetry = { onEvent(TaskListUiEvent.OnRefresh) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                // Success
                is TaskContentState.Success -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing, // Loading'i zaten yukarıda yönetiyoruz
                        onRefresh = { onEvent(TaskListUiEvent.OnRefresh) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TaskList(tasks = content.tasks)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<TaskModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = tasks,
            key = { task -> task.taskId }
        ) { task ->
            TaskRowItem(task = task)
        }
    }
}

@Composable
fun EmptyView(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Tasks not found.")
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}