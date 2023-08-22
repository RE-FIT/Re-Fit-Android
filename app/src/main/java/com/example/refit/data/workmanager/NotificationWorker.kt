package com.example.refit.data.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.refit.data.datastore.NotificationStore

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val notificationStore: NotificationStore
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val roomId = inputData.getString("roomId") ?: return Result.failure()
        val notificationId = inputData.getInt("notificationId", 0)

        notificationStore.addNotificationIdToRoom(roomId, notificationId)
        val notificationsByRoom = notificationStore.getAllNotificationsByRoom(roomId)
        notificationsByRoom?.let {
            Log.d("NotificationService", "Notifications for room $roomId: $it")
        }

        return Result.success()
    }
}