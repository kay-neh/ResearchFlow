package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreSubmissionService
import com.researchflow.app.data.model.Submission
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
}