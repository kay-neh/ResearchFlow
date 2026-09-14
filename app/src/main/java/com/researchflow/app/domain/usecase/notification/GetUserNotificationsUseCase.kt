package com.researchflow.app.domain.usecase.notification

import com.researchflow.app.data.model.Notification
import com.researchflow.app.domain.repository.NotificationRepository
import javax.inject.Inject

class GetUserNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(
        userId: String
    ): List<Notification> {

        if (userId.isBlank()) {
            throw IllegalArgumentException(
                "User is required"
            )
        }

        return notificationRepository.getNotificationsByUser(
            userId
        )
    }
}