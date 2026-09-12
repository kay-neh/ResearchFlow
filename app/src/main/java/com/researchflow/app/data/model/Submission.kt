package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class Submission(
    val submissionId: String = "",
    val studentId: String = "",
    val supervisorId: String = "",
    val title: String = "",
    val status: SubmissionStatus = SubmissionStatus.SUBMITTED,
    val currentVersion: Int = 1,
    val deadlineId: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

enum class SubmissionStatus {
    SUBMITTED,
    UNDER_REVIEW,
    CORRECTION_REQUIRED,
    RESUBMITTED,
    APPROVED,
    REJECTED
}