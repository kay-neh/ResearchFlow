package com.researchflow.app.domain.usecase.notification

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.Notification
import com.researchflow.app.data.model.NotificationType
import com.researchflow.app.domain.repository.NotificationRepository
import java.util.UUID
import javax.inject.Inject

class CreateNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(
        userId: String,
        title: String,
        message: String,
        type: NotificationType = NotificationType.GENERAL,
        submissionId: String? = null,
        deadlineId: String? = null
    ) {

        if (userId.isBlank()) {
            throw IllegalArgumentException(
                "User is required"
            )
        }

        if (title.isBlank()) {
            throw IllegalArgumentException(
                "Notification title is required"
            )
        }

        if (message.isBlank()) {
            throw IllegalArgumentException(
                "Notification message is required"
            )
        }

        val notification = Notification(
            notificationId = UUID.randomUUID().toString(),
            userId = userId,
            title = title.trim(),
            message = message.trim(),
            type = type,
            submissionId = submissionId,
            deadlineId = deadlineId,
            isRead = false,
            createdAt = Timestamp.now()
        )

        notificationRepository.createNotification(
            notification
        )
    }
}