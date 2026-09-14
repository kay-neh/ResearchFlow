package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class GetSubmissionUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository
) {

    suspend operator fun invoke(
        submissionId: String
    ): Submission? {
        return submissionRepository.getSubmission(submissionId)
    }
}