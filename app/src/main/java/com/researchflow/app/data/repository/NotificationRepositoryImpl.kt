package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreNotificationService
import com.researchflow.app.data.model.Notification
import com.researchflow.app.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val firestoreNotificationService: FirestoreNotificationService
) : NotificationRepository {

    override suspend fun createNotification(
        notification: Notification
    ) {
        firestoreNotificationService.createNotification(
            notification
        )
    }

    override suspend fun getNotification(
        notificationId: String
    ): Notification? {
        return firestoreNotificationService.getNotification(
            notificationId
        )
    }

    override suspend fun getNotificationsByUser(
        userId: String
    ): List<Notification> {
        return firestoreNotificationService.getNotificationsByUser(
            userId
        )
    }

    override suspend fun markAsRead(
        notificationId: String
    ) {
        firestoreNotificationService.markAsRead(
            notificationId
        )
    }
}