package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreSubmissionVersionService
import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.domain.repository.SubmissionVersionRepository
import javax.inject.Inject

class SubmissionVersionRepositoryImpl @Inject constructor(
    private val firestoreSubmissionVersionService: FirestoreSubmissionVersionService
) : SubmissionVersionRepository {

    override suspend fun createVersion(version: SubmissionVersion) {
        firestoreSubmissionVersionService.createVersion(version)
    }

    override suspend fun getVersion(
        versionId: String
    ): SubmissionVersion? {
        return firestoreSubmissionVersionService.getVersion(versionId)
    }
}