package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.NotificationType
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.domain.repository.ResearchArchiveRepository
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.domain.usecase.notification.CreateNotificationUseCase
import com.google.firebase.Timestamp
import java.util.UUID
import javax.inject.Inject

class UpdateSubmissionStatusUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository,
    private val researchArchiveRepository: ResearchArchiveRepository,
    private val getSubmissionVersionsUseCase: GetSubmissionVersionsUseCase,
    private val createNotificationUseCase: CreateNotificationUseCase
) {

    suspend operator fun invoke(
        submissionId: String,
        currentStatus: SubmissionStatus,
        newStatus: SubmissionStatus
    ) {
        if (!isValidTransition(currentStatus, newStatus)) {
            throw IllegalStateException(
                "Invalid submission status transition: " +
                        "$currentStatus → $newStatus"
            )
        }

        val submission = submissionRepository.getSubmission(
            submissionId
        ) ?: throw IllegalStateException(
            "Submission not found"
        )

        val student = studentRepository.getStudent(
            submission.studentId
        ) ?: throw IllegalStateException(
            "Student not found"
        )

        // Update the submission status
        submissionRepository.updateSubmissionStatus(
            submissionId = submissionId,
            status = newStatus
        )

        // Archive the approved version
        if (newStatus == SubmissionStatus.APPROVED) {
            val versions = getSubmissionVersionsUseCase(
                submissionId
            )

            val approvedVersion = versions.firstOrNull {
                it.versionNumber == submission.currentVersion
            } ?: throw IllegalStateException(
                "Approved submission version not found"
            )

            val supervisorId = submission.supervisorId

            val archive = com.researchflow.app.data.model.ResearchArchive(
                archiveId = UUID.randomUUID().toString(),
                submissionId = submission.submissionId,
                studentId = submission.studentId,
                supervisorId = supervisorId,
                title = submission.title,
                documentName = approvedVersion.documentName,
                storagePath = approvedVersion.storagePath,
                approvedVersion = approvedVersion.versionNumber,
                archivedAt = Timestamp.now(),
                archivedBy = supervisorId
            )

            researchArchiveRepository.createArchive(archive)
        }

        // Create notification for the student
        createNotificationUseCase(
            userId = student.userId,
            title = getNotificationTitle(newStatus),
            message = getNotificationMessage(newStatus),
            type = NotificationType.SUBMISSION_STATUS,
            submissionId = submissionId
        )
    }

    private fun getNotificationTitle(
        status: SubmissionStatus
    ): String {
        return when (status) {
            SubmissionStatus.UNDER_REVIEW ->
                "Submission Under Review"

            SubmissionStatus.CORRECTION_REQUIRED ->
                "Correction Required"

            SubmissionStatus.RESUBMITTED ->
                "Submission Resubmitted"

            SubmissionStatus.APPROVED ->
                "Submission Approved"

            SubmissionStatus.REJECTED ->
                "Submission Rejected"

            SubmissionStatus.SUBMITTED ->
                "Submission Submitted"
        }
    }

    private fun getNotificationMessage(
        status: SubmissionStatus
    ): String {
        return when (status) {
            SubmissionStatus.UNDER_REVIEW ->
                "Your research submission is now under review by your supervisor."

            SubmissionStatus.CORRECTION_REQUIRED ->
                "Your supervisor has requested corrections to your research submission."

            SubmissionStatus.RESUBMITTED ->
                "Your corrected research submission has been resubmitted for review."

            SubmissionStatus.APPROVED ->
                "Your research submission has been approved by your supervisor."

            SubmissionStatus.REJECTED ->
                "Your research submission has been rejected by your supervisor."

            SubmissionStatus.SUBMITTED ->
                "Your research submission has been submitted."
        }
    }

    private fun isValidTransition(
        currentStatus: SubmissionStatus,
        newStatus: SubmissionStatus
    ): Boolean {
        return when (currentStatus) {

            SubmissionStatus.SUBMITTED ->
                newStatus == SubmissionStatus.UNDER_REVIEW

            SubmissionStatus.UNDER_REVIEW ->
                newStatus == SubmissionStatus.CORRECTION_REQUIRED ||
                        newStatus == SubmissionStatus.APPROVED ||
                        newStatus == SubmissionStatus.REJECTED

            SubmissionStatus.CORRECTION_REQUIRED ->
                newStatus == SubmissionStatus.RESUBMITTED

            SubmissionStatus.RESUBMITTED ->
                newStatus == SubmissionStatus.UNDER_REVIEW

            SubmissionStatus.APPROVED ->
                false

            SubmissionStatus.REJECTED ->
                false
        }
    }
}