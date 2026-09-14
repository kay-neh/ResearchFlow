package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class Notification(
    val notificationId: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.GENERAL,
    val submissionId: String? = null,
    val deadlineId: String? = null,
    val isRead: Boolean = false,
    val createdAt: Timestamp? = null
)

enum class NotificationType {
    GENERAL,
    DEADLINE_CREATED,
    DEADLINE_UPDATED,
    DEADLINE_REMINDER,
    SUBMISSION_STATUS,
    FEEDBACK
}