package com.enesay.android_task

import app.cash.turbine.test
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.usecase.GetTasksUseCase
import com.enesay.android_task.domain.usecase.RefreshTasksUseCase
import com.enesay.android_task.domain.usecase.SearchTasksUseCase
import com.enesay.android_task.ui.screens.TaskList.TaskContentState
import com.enesay.android_task.ui.screens.TaskList.TaskListEffect
import com.enesay.android_task.ui.screens.TaskList.TaskListUiEvent
import com.enesay.android_task.ui.screens.TaskList.TaskListViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import io.mockk.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mocks
    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val searchTasksUseCase: SearchTasksUseCase = mockk()
    private val refreshTasksUseCase: RefreshTasksUseCase = mockk()

    private lateinit var viewModel: TaskListViewModel

    // Use StandardTestDispatcher to control virtual time
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Default mock behaviors
        every { getTasksUseCase() } returns flowOf(emptyList())
        coEvery { refreshTasksUseCase() } just Runs

        viewModel = TaskListViewModel(
            getTasksUseCase,
            searchTasksUseCase,
            refreshTasksUseCase,
            testDispatcher
        )
    }

    @Test
    fun `init block triggers initial sync`() = runTest(testDispatcher) {
        advanceUntilIdle()
        coVerify(exactly = 1) { refreshTasksUseCase() }
    }

    @Test
    fun `search query updates and respects debounce`() = runTest(testDispatcher) {
        val mockList = listOf(TaskModel("1", "Task", "Title", "Desc", "#FFFFFF"))
        every { searchTasksUseCase("gert") } returns flowOf(mockList)

        // FIX: Use backgroundScope and collect {}
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        // Trigger search
        viewModel.onEvent(TaskListUiEvent.OnSearchQueryChange("gert"))

        // Before 300ms
        advanceTimeBy(200)
        verify(exactly = 0) { searchTasksUseCase("gert") }

        // After 300ms
        advanceTimeBy(101)
        advanceUntilIdle()
        verify(exactly = 1) { searchTasksUseCase("gert") }

        val content = viewModel.uiState.value.contentState
        assertTrue(content is TaskContentState.Success)
        assertEquals(mockList, (content as TaskContentState.Success).tasks)
    }

    @Test
    fun `search query short length is filtered out`() = runTest(testDispatcher) {
        // FIX: Use backgroundScope
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(TaskListUiEvent.OnSearchQueryChange("a"))
        advanceUntilIdle()

        verify(exactly = 0) { searchTasksUseCase(any()) }
    }

    @Test
    fun `refresh failure emits toast effect`() = runTest(testDispatcher) {
        val errorMsg = "Network Error"
        coEvery { refreshTasksUseCase() } throws RuntimeException(errorMsg)

        viewModel.effect.test {
            viewModel.onEvent(TaskListUiEvent.OnRefresh)
            advanceUntilIdle()

            val item = awaitItem()
            assertTrue(item is TaskListEffect.ShowToast)
            assertEquals(errorMsg, (item as TaskListEffect.ShowToast).message)
        }

        assertEquals(false, viewModel.uiState.value.isRefreshing)
    }
}