package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreResearchArchiveService
import com.researchflow.app.data.model.ResearchArchive
import com.researchflow.app.domain.repository.ResearchArchiveRepository
import javax.inject.Inject

class ResearchArchiveRepositoryImpl @Inject constructor(
    private val firestoreResearchArchiveService: FirestoreResearchArchiveService
) : ResearchArchiveRepository {

    override suspend fun createArchive(
        archive: ResearchArchive
    ) {
        firestoreResearchArchiveService.createArchive(archive)
    }

    override suspend fun getArchive(
        archiveId: String
    ): ResearchArchive? {
        return firestoreResearchArchiveService.getArchive(
            archiveId
        )
    }

    override suspend fun getAllArchives(): List<ResearchArchive> {
        return firestoreResearchArchiveService.getAllArchives()
    }

    override suspend fun getArchivesByStudent(
        studentId: String
    ): List<ResearchArchive> {
        return firestoreResearchArchiveService.getArchivesByStudent(
            studentId
        )
    }
}