package com.example.refit.data.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.refit.data.datastore.NotificationStore
import org.koin.core.Koin

class KoinWorkerFactory(private val koin: Koin) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            NotificationWorker::class.java.name -> {
                val notificationStore: NotificationStore = koin.get()
                NotificationWorker(appContext, workerParameters, notificationStore)
            }
            else -> null
        }
    }
}