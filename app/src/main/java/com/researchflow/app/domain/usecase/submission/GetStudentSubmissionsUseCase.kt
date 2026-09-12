package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class GetStudentSubmissionsUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository
) {
    suspend operator fun invoke(
        studentId: String
    ): List<Submission> {
        return submissionRepository.getSubmissionsByStudent(
            studentId
        )
    }
}