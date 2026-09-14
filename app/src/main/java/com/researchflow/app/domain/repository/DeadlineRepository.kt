package com.researchflow.app.domain.repository

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.Deadline

interface DeadlineRepository {

    suspend fun createDeadline(deadline: Deadline)

    suspend fun getDeadline(deadlineId: String): Deadline?

    suspend fun getDeadlineBySubmission(
        submissionId: String
    ): Deadline?

    suspend fun getDeadlinesBySupervisor(
        supervisorId: String
    ): List<Deadline>

    suspend fun updateDeadline(
        deadlineId: String,
        title: String,
        description: String,
        deadlineDate: Timestamp
    )

    suspend fun setDeadlineActive(
        deadlineId: String,
        isActive: Boolean
    )
}