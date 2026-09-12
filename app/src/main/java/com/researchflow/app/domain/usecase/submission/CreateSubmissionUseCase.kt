package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class CreateSubmissionUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository
) {

    suspend operator fun invoke(submission: Submission) {
        submissionRepository.createSubmission(submission)
    }
}