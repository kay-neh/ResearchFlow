package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.Submission

interface SubmissionRepository {

    suspend fun createSubmission(submission: Submission)

    suspend fun getSubmission(submissionId: String): Submission?
}