package com.enesay.android_task

import app.cash.turbine.test
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.usecase.GetTasksUseCase
import com.enesay.android_task.domain.usecase.RefreshTasksUseCase
import com.enesay.android_task.domain.usecase.SearchTasksUseCase
import com.enesay.android_task.ui.screens.TaskList.TaskListViewModel
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class TaskListViewModelTest {

    private lateinit var taskListViewModel: TaskListViewModel
    private val getTasksUseCase = mockk<GetTasksUseCase>()
    private val searchTasksUseCase = mockk<SearchTasksUseCase>()
    private val refreshTasksUseCase = mockk<RefreshTasksUseCase>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Setting the proper dispatcher
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `tasks flow emits data when collected`() = runTest {
        // --- ARRANGE ---
        val expectedTaskList = listOf(
            TaskModel("1", "task1", "task1 title", "task1 desc", "#fffff"),
            TaskModel("2", "task2", "task2 title", "task2 desc", "#ffwsgf")
        )

        // Mock Behavior
        every { getTasksUseCase.invoke() } returns flowOf(expectedTaskList)

        taskListViewModel = TaskListViewModel(
            getTasksUseCase,
            searchTasksUseCase,
            refreshTasksUseCase,
            ioDispatcher = testDispatcher
        )

        // --- ACT & ASSERT ---
        taskListViewModel.tasks.test {

            val firstData = awaitItem()
            Truth.assertThat(firstData).isEmpty() // Initial value for state flow is empty list

            val receivedData = awaitItem()
            Truth.assertThat(receivedData).isEqualTo(expectedTaskList)

            cancelAndIgnoreRemainingEvents()
        }
    }
}