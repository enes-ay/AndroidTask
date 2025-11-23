package com.enesay.android_task.data.repository

import com.enesay.android_task.data.LoginRequest
import com.enesay.android_task.data.local.TaskDAO
import com.enesay.android_task.data.mapper.toDomain
import com.enesay.android_task.data.mapper.toEntity
import com.enesay.android_task.data.remote.ApiService
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.repository.TaskRepository
import com.enesay.android_task.utils.PASSWORD
import com.enesay.android_task.utils.TokenDatastore
import com.enesay.android_task.utils.USERNAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDAO,
    private val apiService: ApiService,
    private val dataStoreManager: TokenDatastore
) : TaskRepository {

    override fun getTasks(): Flow<List<TaskModel>> {
        return taskDao.getTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchTask(query: String): Flow<List<TaskModel>> {
        return taskDao.searchTasks(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateTasks() {
        try {
            fetchAndSaveFromRemote()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                performAutoLoginAndRetry()
            } else {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun performAutoLoginAndRetry() {
        try {
            val loginResponse = apiService.login(
                LoginRequest(USERNAME, PASSWORD)
            )
            if (loginResponse.oauth != null) {
                val newToken = loginResponse.oauth.access_token
                if (newToken != null) {
                    dataStoreManager.saveToken(newToken)
                    fetchAndSaveFromRemote()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchAndSaveFromRemote() {
        val tasks = apiService.getTasks()
        val entities = tasks.map { it.toEntity() }
        taskDao.updateTasks(entities)
    }
}