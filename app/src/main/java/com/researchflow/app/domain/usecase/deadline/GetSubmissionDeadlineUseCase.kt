package com.researchflow.app.domain.usecase.deadline

import com.researchflow.app.data.model.Deadline
import com.researchflow.app.domain.repository.DeadlineRepository
import javax.inject.Inject

class GetSubmissionDeadlineUseCase @Inject constructor(
    private val deadlineRepository: DeadlineRepository
) {
    suspend operator fun invoke(
        deadlineId: String?
    ): Deadline? {

        if (deadlineId.isNullOrBlank()) {
            return null
        }

        return deadlineRepository.getDeadline(deadlineId)
    }
}