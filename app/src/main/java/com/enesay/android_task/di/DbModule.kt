package com.enesay.android_task.di

import android.content.Context
import androidx.room.Room
import com.enesay.android_task.data.local.TaskDAO
import com.enesay.android_task.data.local.TaskDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TaskDB {
        return Room.databaseBuilder(
            context,
            TaskDB::class.java,
            "task.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTaskDao(db: TaskDB): TaskDAO = db.getTaskDAO()
}