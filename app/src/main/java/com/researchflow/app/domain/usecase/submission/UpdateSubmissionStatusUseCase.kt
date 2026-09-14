package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class UpdateSubmissionStatusUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository
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

        submissionRepository.updateSubmissionStatus(
            submissionId = submissionId,
            status = newStatus
        )
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