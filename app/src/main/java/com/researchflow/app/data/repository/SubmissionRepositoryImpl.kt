package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreSubmissionService
import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class SubmissionRepositoryImpl @Inject constructor(
    private val firestoreSubmissionService: FirestoreSubmissionService
) : SubmissionRepository {

    override suspend fun createSubmission(submission: Submission) {
        firestoreSubmissionService.createSubmission(submission)
    }

    override suspend fun getSubmission(
        submissionId: String
    ): Submission? {
        return firestoreSubmissionService.getSubmission(submissionId)
    }

    override suspend fun getSubmissionsByStudent(
        studentId: String
    ): List<Submission> {
        return firestoreSubmissionService
            .getSubmissionsByStudent(studentId)
    }

    override suspend fun getSubmissionsBySupervisor(
        supervisorId: String
    ): List<Submission> {
        return firestoreSubmissionService
            .getSubmissionsBySupervisor(supervisorId)
    }

    override suspend fun updateSubmissionStatus(
        submissionId: String,
        status: SubmissionStatus
    ) {
        firestoreSubmissionService.updateSubmissionStatus(
            submissionId = submissionId,
            status = status
        )
    }

    override suspend fun updateSubmissionForResubmission(
        submissionId: String,
        currentVersion: Int
    ) {
        firestoreSubmissionService.updateSubmissionForResubmission(
            submissionId = submissionId,
            currentVersion = currentVersion
        )
    }

    override suspend fun updateDeadlineId(
        submissionId: String,
        deadlineId: String?
    ) {
        firestoreSubmissionService.updateDeadlineId(
            submissionId = submissionId,
            deadlineId = deadlineId
        )
    }
}