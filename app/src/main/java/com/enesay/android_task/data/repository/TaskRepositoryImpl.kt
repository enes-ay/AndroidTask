package com.enesay.android_task.data.repository

import com.enesay.android_task.data.local.TaskDAO
import com.enesay.android_task.data.mapper.toDomain
import com.enesay.android_task.data.mapper.toEntity
import com.enesay.android_task.data.remote.ApiService
import com.enesay.android_task.domain.model.TaskModel
import com.enesay.android_task.domain.repository.AuthRepository
import com.enesay.android_task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDAO,
    private val apiService: ApiService,
    private val authRepository: AuthRepository
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

        val isAuthReady = authRepository.ensureValidToken()

        if (!isAuthReady) {
            throw Exception("Invalid Session, login again!")
        }

        try {
            val remoteTasks = apiService.getTasks()
            val entities = remoteTasks.map { it.toEntity() }
            taskDao.updateTasks(entities)

        } catch (e: HttpException) {
            if (e.code() == 401) {
                // performAutoLoginAndRetry()
            }
            throw e
        }
    }
}