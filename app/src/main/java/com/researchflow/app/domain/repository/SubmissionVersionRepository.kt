package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.SubmissionVersion

interface SubmissionVersionRepository {

    suspend fun createVersion(version: SubmissionVersion)

    suspend fun getVersion(versionId: String): SubmissionVersion?
}