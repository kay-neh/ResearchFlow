package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.researchflow.app.data.model.Notification
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreNotificationService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createNotification(
        notification: Notification
    ) {
        firestore.collection("notifications")
            .document(notification.notificationId)
            .set(notification)
            .await()
    }

    suspend fun getNotification(
        notificationId: String
    ): Notification? {
        return firestore.collection("notifications")
            .document(notificationId)
            .get()
            .await()
            .toObject(Notification::class.java)
    }

    suspend fun getNotificationsByUser(
        userId: String
    ): List<Notification> {
        val snapshot = firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->

            val notificationId = document.id
            val documentUserId = document.getString("userId") ?: return@mapNotNull null
            val title = document.getString("title") ?: ""
            val message = document.getString("message") ?: ""

            val type = document.getString("type")
                ?.let {
                    runCatching {
                        com.researchflow.app.data.model.NotificationType.valueOf(it)
                    }.getOrDefault(
                        com.researchflow.app.data.model.NotificationType.GENERAL
                    )
                }
                ?: com.researchflow.app.data.model.NotificationType.GENERAL

            val submissionId = document.getString("submissionId")
            val deadlineId = document.getString("deadlineId")

            val isRead = document.getBoolean("isRead") ?: false

            val createdAt = document.getTimestamp("createdAt")

            Notification(
                notificationId = notificationId,
                userId = documentUserId,
                title = title,
                message = message,
                type = type,
                submissionId = submissionId,
                deadlineId = deadlineId,
                isRead = isRead,
                createdAt = createdAt
            )
        }
    }

    suspend fun markAsRead(
        notificationId: String
    ) {
        firestore.collection("notifications")
            .document(notificationId)
            .update("isRead", true)
            .await()
    }
}