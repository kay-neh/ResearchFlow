package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.ResearchArchive

interface ResearchArchiveRepository {

    suspend fun createArchive(
        archive: ResearchArchive
    )

    suspend fun getArchive(
        archiveId: String
    ): ResearchArchive?

    suspend fun getAllArchives(): List<ResearchArchive>

    suspend fun getArchivesByStudent(
        studentId: String
    ): List<ResearchArchive>
}