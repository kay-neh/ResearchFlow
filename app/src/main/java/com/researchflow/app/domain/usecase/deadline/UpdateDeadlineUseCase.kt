package com.researchflow.app.domain.usecase.deadline

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.NotificationType
import com.researchflow.app.domain.repository.DeadlineRepository
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.domain.usecase.notification.CreateNotificationUseCase
import javax.inject.Inject

class UpdateDeadlineUseCase @Inject constructor(
    private val deadlineRepository: DeadlineRepository,
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository,
    private val createNotificationUseCase: CreateNotificationUseCase
) {

    suspend operator fun invoke(
        deadlineId: String,
        title: String,
        description: String,
        deadlineDate: Timestamp
    ) {
        if (deadlineId.isBlank()) {
            throw IllegalArgumentException("Deadline is required")
        }

        if (title.isBlank()) {
            throw IllegalArgumentException("Deadline title is required")
        }

        val deadline = deadlineRepository.getDeadline(
            deadlineId
        ) ?: throw IllegalStateException("Deadline not found")

        val submission = submissionRepository.getSubmission(
            deadline.submissionId
        ) ?: throw IllegalStateException("Submission not found")

        val student = studentRepository.getStudent(
            submission.studentId
        ) ?: throw IllegalStateException("Student not found")

        deadlineRepository.updateDeadline(
            deadlineId = deadlineId,
            title = title.trim(),
            description = description.trim(),
            deadlineDate = deadlineDate
        )

        createNotificationUseCase(
            userId = student.userId,
            title = "Deadline Updated",
            message = "The deadline for your research submission has been updated.",
            type = NotificationType.DEADLINE_UPDATED,
            submissionId = deadline.submissionId,
            deadlineId = deadlineId
        )
    }
}