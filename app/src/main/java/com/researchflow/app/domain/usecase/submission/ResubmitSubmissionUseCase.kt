package com.researchflow.app.domain.usecase.submission

import android.net.Uri
import com.researchflow.app.data.model.DocumentType
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.data.model.SubmissionStatus
import javax.inject.Inject

class ResubmitSubmissionUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository,
    private val uploadSubmissionVersionUseCase: UploadSubmissionVersionUseCase
) {

    suspend operator fun invoke(
        submissionId: String,
        studentId: String,
        currentVersion: Int,
        fileUri: Uri,
        fileName: String,
        documentType: DocumentType
    ) {
        val submission = submissionRepository
            .getSubmission(submissionId)
            ?: throw IllegalStateException(
                "Submission not found"
            )

        if (submission.studentId != studentId) {
            throw IllegalStateException(
                "You are not allowed to resubmit this submission."
            )
        }

        if (submission.status != SubmissionStatus.CORRECTION_REQUIRED
        ) {
            throw IllegalStateException(
                "This submission is not awaiting corrections."
            )
        }

        val nextVersion = currentVersion + 1

        uploadSubmissionVersionUseCase(
            studentId = studentId,
            submissionId = submissionId,
            versionNumber = nextVersion,
            fileUri = fileUri,
            fileName = fileName,
            documentType = documentType
        )

        submissionRepository.updateSubmissionForResubmission(
            submissionId = submissionId,
            currentVersion = nextVersion
        )
    }
}