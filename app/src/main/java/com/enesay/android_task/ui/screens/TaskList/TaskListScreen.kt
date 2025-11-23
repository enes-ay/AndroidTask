package com.enesay.android_task.ui.screens.TaskList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.ui.components.DefaultTopBar
import com.enesay.android_task.ui.components.SearchTopBar
import com.enesay.android_task.ui.components.TaskRowItem
import com.enesay.android_task.utils.startQrCodeScanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel(), innerPadding: PaddingValues) {
    val tasks by viewModel.tasks.collectAsState()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    var isSearchActive by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            // Deciding which topbar will be used
            if (isSearchActive) {
                SearchTopBar(text = searchQuery, onTextChange = {
                    searchQuery = it
                    viewModel.setQuery(it)
                }, onCloseClicked = {
                    isSearchActive = false
                    viewModel.setQuery("")
                }, onQrClicked = {
                    startQrCodeScanner(
                        context = context,
                        onResult = {
                            searchQuery = it
                            viewModel.setQuery(it)
                        })
                })
            } else {
                DefaultTopBar(onSearchClicked = { isSearchActive = true }, onQrClicked = {
                    startQrCodeScanner(
                        context = context,
                        onResult = {
                            isSearchActive = true
                            searchQuery = it
                            viewModel.setQuery(it)
                        })
                })
            }
        }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.weight(1f)
            ) {
                TaskList(tasks = tasks)
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<TaskModel>, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(tasks) { task ->
            TaskRowItem(task = task)
        }
    }
}