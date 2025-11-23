package com.enesay.android_task.di

import com.enesay.android_task.data.repository.TaskRepositoryImpl
import com.enesay.android_task.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository = impl
}
