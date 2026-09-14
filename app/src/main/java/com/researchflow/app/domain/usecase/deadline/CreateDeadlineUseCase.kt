package com.researchflow.app.domain.usecase.deadline

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.Deadline
import com.researchflow.app.data.model.NotificationType
import com.researchflow.app.domain.repository.DeadlineRepository
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.domain.usecase.notification.CreateNotificationUseCase
import java.util.UUID
import javax.inject.Inject

class CreateDeadlineUseCase @Inject constructor(
    private val deadlineRepository: DeadlineRepository,
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository,
    private val createNotificationUseCase: CreateNotificationUseCase
) {

    suspend operator fun invoke(
        submissionId: String,
        supervisorId: String,
        title: String,
        description: String,
        deadlineDate: Timestamp
    ) {
        if (submissionId.isBlank()) {
            throw IllegalArgumentException("Submission is required")
        }

        if (supervisorId.isBlank()) {
            throw IllegalArgumentException("Supervisor is required")
        }

        if (title.isBlank()) {
            throw IllegalArgumentException("Deadline title is required")
        }

        val submission =
            submissionRepository.getSubmission(submissionId)
                ?: throw IllegalArgumentException(
                    "Submission not found"
                )

        val student =
            studentRepository.getStudent(submission.studentId)
                ?: throw IllegalArgumentException(
                    "Student not found"
                )

        val deadline = Deadline(
            deadlineId = UUID.randomUUID().toString(),
            submissionId = submissionId,
            supervisorId = supervisorId,
            title = title.trim(),
            description = description.trim(),
            deadlineDate = deadlineDate,
            createdAt = Timestamp.now(),
            isActive = true
        )

        deadlineRepository.createDeadline(deadline)

        submissionRepository.updateDeadlineId(
            submissionId = submissionId,
            deadlineId = deadline.deadlineId
        )

        createNotificationUseCase(
            userId = student.userId,
            title = "New Deadline",
            message = "A new deadline has been set for your research submission: ${deadline.title}",
            type = NotificationType.DEADLINE_CREATED,
            submissionId = submissionId,
            deadlineId = deadline.deadlineId
        )

    }
}