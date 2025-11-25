package com.enesay.android_task.utils

import android.content.Context
import androidx.work.*
import com.enesay.android_task.data.worker.SyncTasksWorker
import java.util.concurrent.TimeUnit

object WorkManagerScheduler {

    private const val SYNC_WORK_NAME = "sync_tasks_work"

    fun schedulePeriodicSync(context: Context) {
        // It works only we have Internet
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncTasksWorker>(
            60, TimeUnit.MINUTES,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}