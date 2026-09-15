package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.domain.repository.SubmissionVersionRepository
import javax.inject.Inject

class CreateSignedDocumentUrlUseCase @Inject constructor(
    private val submissionVersionRepository: SubmissionVersionRepository
) {

    suspend operator fun invoke(
        storagePath: String
    ): String {
        require(storagePath.isNotBlank()) {
            "Document storage path cannot be empty"
        }

        return submissionVersionRepository
            .createSignedDocumentUrl(storagePath)
    }
}
