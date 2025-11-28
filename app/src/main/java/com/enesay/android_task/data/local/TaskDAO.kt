package com.enesay.android_task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(taskList: List<TaskEntity>)

    @Query("DELETE FROM tasks")
    suspend fun deleteTasks()

    @Query("""
    SELECT * FROM tasks
    WHERE LOWER(
        IFNULL(id, '') || ' ' ||
        IFNULL(task, '') || ' ' ||
        IFNULL(title, '') || ' ' ||
        IFNULL(description, '') || ' ' ||
        IFNULL(sort, '') || ' ' ||
        IFNULL(wageType, '') || ' ' ||
        IFNULL(businessUnitKey, '') || ' ' ||
        IFNULL(businessUnit, '') || ' ' ||
        IFNULL(parentTaskID, '') || ' ' ||
        IFNULL(preplanningBoardQuickSelect, '') || ' ' ||
        IFNULL(colorCode, '') || ' ' ||
        IFNULL(workingTime, '') || ' ' ||
        IFNULL(externalId, '')
    ) LIKE '%' || LOWER(:query) || '%'
""")
    fun searchTasks(query: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id NOT IN (:activeTaskIds)")
    suspend fun deleteTasksNotIn(activeTaskIds: List<String>)

    @Transaction
    suspend fun updateTasks(tasks: List<TaskEntity>) {
        val activeIds = tasks.map { it.id } // the IDs still exist in back-end
        deleteTasksNotIn(activeIds)  // back-end sync
        insertAllTasks(tasks)
    }

}