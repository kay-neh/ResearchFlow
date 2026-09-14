package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionStatus

interface SubmissionRepository {

    suspend fun createSubmission(submission: Submission)

    suspend fun getSubmission(submissionId: String): Submission?

    suspend fun getSubmissionsByStudent(
        studentId: String
    ): List<Submission>

    suspend fun getSubmissionsBySupervisor(
        supervisorId: String
    ): List<Submission>

    suspend fun updateSubmissionStatus(
        submissionId: String,
        status: SubmissionStatus
    )

    suspend fun updateSubmissionForResubmission(
        submissionId: String,
        currentVersion: Int
    )
}