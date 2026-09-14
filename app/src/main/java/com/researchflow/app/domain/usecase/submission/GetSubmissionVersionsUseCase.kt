package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.domain.repository.SubmissionVersionRepository
import javax.inject.Inject

class GetSubmissionVersionsUseCase @Inject constructor(
    private val submissionVersionRepository: SubmissionVersionRepository
) {

    suspend operator fun invoke(
        submissionId: String
    ): List<SubmissionVersion> {
        return submissionVersionRepository.getVersionsBySubmission(
            submissionId
        )
    }
}