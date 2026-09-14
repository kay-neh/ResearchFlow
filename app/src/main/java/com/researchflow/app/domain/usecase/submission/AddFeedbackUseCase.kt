package com.researchflow.app.domain.usecase.submission

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.Feedback
import com.researchflow.app.data.model.NotificationType
import com.researchflow.app.domain.repository.FeedbackRepository
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.domain.usecase.notification.CreateNotificationUseCase
import java.util.UUID
import javax.inject.Inject

class AddFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository,
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository,
    private val createNotificationUseCase: CreateNotificationUseCase
) {

    suspend operator fun invoke(
        submissionId: String,
        versionId: String,
        supervisorId: String,
        comment: String
    ) {
        if (comment.isBlank()) {
            throw IllegalArgumentException("Feedback cannot be empty")
        }

        if (versionId.isBlank()) {
            throw IllegalArgumentException("Submission version is required")
        }

        val submission = submissionRepository.getSubmission(
            submissionId
        ) ?: throw IllegalStateException("Submission not found")

        val student = studentRepository.getStudent(
            submission.studentId
        ) ?: throw IllegalStateException("Student not found")

        val feedback = Feedback(
            feedbackId = UUID.randomUUID().toString(),
            submissionId = submissionId,
            versionId = versionId,
            supervisorId = supervisorId,
            comment = comment.trim(),
            createdAt = Timestamp.now()
        )

        feedbackRepository.createFeedback(feedback)

        createNotificationUseCase(
            userId = student.userId,
            title = "New Supervisor Feedback",
            message = "Your supervisor has added new feedback to your research submission.",
            type = NotificationType.FEEDBACK,
            submissionId = submissionId
        )
    }
}