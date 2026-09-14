package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.domain.repository.SubmissionVersionRepository
import javax.inject.Inject

class OpenSubmissionDocumentUseCase @Inject constructor(
    private val submissionVersionRepository: SubmissionVersionRepository
) {

    suspend operator fun invoke(
        storagePath: String
    ): String {
        if (storagePath.isBlank()) {
            throw IllegalArgumentException(
                "Document storage path is missing"
            )
        }

        return submissionVersionRepository
            .createSignedDocumentUrl(storagePath)
    }
}