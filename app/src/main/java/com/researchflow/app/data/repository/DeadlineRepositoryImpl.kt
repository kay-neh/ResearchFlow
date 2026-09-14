package com.researchflow.app.data.repository

import com.google.firebase.Timestamp
import com.researchflow.app.data.firebase.FirestoreDeadlineService
import com.researchflow.app.data.model.Deadline
import com.researchflow.app.domain.repository.DeadlineRepository
import javax.inject.Inject

class DeadlineRepositoryImpl @Inject constructor(
    private val firestoreDeadlineService: FirestoreDeadlineService
) : DeadlineRepository {

    override suspend fun createDeadline(deadline: Deadline) {
        firestoreDeadlineService.createDeadline(deadline)
    }

    override suspend fun getDeadline(
        deadlineId: String
    ): Deadline? {
        return firestoreDeadlineService.getDeadline(deadlineId)
    }

    override suspend fun getDeadlineBySubmission(
        submissionId: String
    ): Deadline? {
        return firestoreDeadlineService.getDeadlineBySubmission(
            submissionId
        )
    }

    override suspend fun getDeadlinesBySupervisor(
        supervisorId: String
    ): List<Deadline> {
        return firestoreDeadlineService.getDeadlinesBySupervisor(
            supervisorId
        )
    }

    override suspend fun updateDeadline(
        deadlineId: String,
        title: String,
        description: String,
        deadlineDate: Timestamp
    ) {
        firestoreDeadlineService.updateDeadline(
            deadlineId = deadlineId,
            title = title,
            description = description,
            deadlineDate = deadlineDate
        )
    }

    override suspend fun setDeadlineActive(
        deadlineId: String,
        isActive: Boolean
    ) {
        firestoreDeadlineService.setDeadlineActive(
            deadlineId = deadlineId,
            isActive = isActive
        )
    }
}