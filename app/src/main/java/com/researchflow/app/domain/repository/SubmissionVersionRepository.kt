package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.SubmissionVersion

interface SubmissionVersionRepository {

    suspend fun createVersion(version: SubmissionVersion)

    suspend fun getVersion(versionId: String): SubmissionVersion?

    suspend fun getVersionsBySubmission(
        submissionId: String
    ): List<SubmissionVersion>

    suspend fun createSignedDocumentUrl(
        storagePath: String
    ): String
}