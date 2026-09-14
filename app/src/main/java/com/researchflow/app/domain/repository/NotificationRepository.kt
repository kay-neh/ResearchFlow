package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.Notification

interface NotificationRepository {

    suspend fun createNotification(
        notification: Notification
    )

    suspend fun getNotification(
        notificationId: String
    ): Notification?

    suspend fun getNotificationsByUser(
        userId: String
    ): List<Notification>

    suspend fun markAsRead(
        notificationId: String
    )
}