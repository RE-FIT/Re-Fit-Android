package com.example.refit.data.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class NotificationStore(private val dataStore: DataStore<Notifications>) {

    // 1. NotificationsByRoom에 notification_ids를 추가하는 기능
    suspend fun addNotificationIdToRoom(roomId: String, notificationId: Int) {
        dataStore.updateData { currentNotifications ->
            val currentIdsByRoom = currentNotifications.notificationsByRoom[roomId]
            val updatedIdsByRoom = if (currentIdsByRoom != null) {
                NotificationsByRoom.newBuilder(currentIdsByRoom)
                    .addNotificationIds(notificationId)
                    .build()
            } else {
                NotificationsByRoom.newBuilder()
                    .addNotificationIds(notificationId)
                    .build()
            }

            val updatedNotifications = Notifications.newBuilder(currentNotifications)
                .putNotificationsByRoom(roomId, updatedIdsByRoom)
                .build()

            updatedNotifications
        }
    }

    // 2. Notifications에서 roomId에 해당하는 모든 NotificationsByRoom를 조회하는 기능
    suspend fun getAllNotificationsByRoom(roomId: String): List<Int>? {
        val notifications = dataStore.data.firstOrNull() ?: return null
        return notifications.notificationsByRoom[roomId]?.notificationIdsList
    }

    // 3. roomId에 해당하는 Notifications을 삭제하는 기능
    suspend fun deleteNotificationsByRoom(roomId: String) {
        dataStore.updateData { currentNotifications ->
            if (currentNotifications.notificationsByRoomMap.containsKey(roomId)) {
                val updatedNotifications = Notifications.newBuilder(currentNotifications)
                    .removeNotificationsByRoom(roomId)
                    .build()
                updatedNotifications
            } else {
                currentNotifications
            }
        }
    }
}