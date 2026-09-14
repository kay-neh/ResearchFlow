package com.researchflow.app.domain.usecase.deadline

import com.researchflow.app.domain.repository.DeadlineRepository
import javax.inject.Inject

class SetDeadlineActiveUseCase @Inject constructor(
    private val deadlineRepository: DeadlineRepository
) {

    suspend operator fun invoke(
        deadlineId: String,
        isActive: Boolean
    ) {
        if (deadlineId.isBlank()) {
            throw IllegalArgumentException("Deadline is required")
        }

        deadlineRepository.setDeadlineActive(
            deadlineId = deadlineId,
            isActive = isActive
        )
    }
}