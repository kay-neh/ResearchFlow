package com.researchflow.app.domain.usecase.deadline

import com.researchflow.app.data.model.Deadline
import com.researchflow.app.domain.repository.DeadlineRepository
import javax.inject.Inject

class GetSupervisorDeadlinesUseCase @Inject constructor(
    private val deadlineRepository: DeadlineRepository
) {

    suspend operator fun invoke(
        supervisorId: String
    ): List<Deadline> {
        return deadlineRepository.getDeadlinesBySupervisor(
            supervisorId
        )
    }
}