package com.enesay.android_task.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enesay.android_task.domain.model.TaskItem

@Database(entities = [TaskItem::class], version = 1)
abstract class TaskDB : RoomDatabase() {
    abstract fun getTaskDAO () : TaskDAO
}