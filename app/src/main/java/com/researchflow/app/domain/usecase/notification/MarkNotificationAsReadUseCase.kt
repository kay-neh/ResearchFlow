package com.researchflow.app.domain.usecase.notification

import com.researchflow.app.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(
        notificationId: String
    ) {

        if (notificationId.isBlank()) {
            throw IllegalArgumentException(
                "Notification is required"
            )
        }

        notificationRepository.markAsRead(
            notificationId
        )
    }
}