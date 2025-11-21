package com.enesay.android_task.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enesay.android_task.domain.model.TaskItem

@Dao
interface TaskDAO {

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<TaskItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskList: List<TaskItem>)

    @Query("DELETE FROM tasks")
    suspend fun deleteTasks()

    @Query(
        """
    SELECT * FROM tasks
    WHERE LOWER(title || ' ' || task || ' ' || description || ' ' || sort)
    LIKE '%' || LOWER(:query) || '%'
"""
    )
    suspend fun searchTasks(query: String): List<TaskItem>

}