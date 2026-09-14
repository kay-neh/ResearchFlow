package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class GetSupervisorSubmissionsUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository
) {

    suspend operator fun invoke(
        supervisorId: String
    ): List<Submission> {
        return submissionRepository.getSubmissionsBySupervisor(
            supervisorId
        )
    }
}