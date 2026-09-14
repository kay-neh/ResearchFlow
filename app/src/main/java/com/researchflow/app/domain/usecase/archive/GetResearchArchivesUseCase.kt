package com.researchflow.app.domain.usecase.archive

import com.researchflow.app.data.model.ResearchArchive
import com.researchflow.app.domain.repository.ResearchArchiveRepository
import javax.inject.Inject

class GetResearchArchivesUseCase @Inject constructor(
    private val researchArchiveRepository: ResearchArchiveRepository
) {

    suspend operator fun invoke(): List<ResearchArchive> {
        return researchArchiveRepository.getAllArchives()
    }
}