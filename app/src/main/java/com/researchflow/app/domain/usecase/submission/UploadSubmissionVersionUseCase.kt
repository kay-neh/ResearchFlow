package com.researchflow.app.domain.usecase.submission

import android.net.Uri
import com.google.firebase.Timestamp
import com.researchflow.app.data.firebase.SupabaseStorageService
import com.researchflow.app.data.model.DocumentType
import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.domain.repository.SubmissionVersionRepository
import java.util.UUID
import javax.inject.Inject

class UploadSubmissionVersionUseCase @Inject constructor(
    private val storageService: SupabaseStorageService,
    private val versionRepository: SubmissionVersionRepository
) {

    suspend operator fun invoke(
        studentId: String,
        submissionId: String,
        versionNumber: Int,
        fileUri: Uri,
        fileName: String,
        documentType: DocumentType
    ) {
        val storagePath = storageService.uploadSubmissionDocument(
            studentId = studentId,
            submissionId = submissionId,
            versionNumber = versionNumber,
            fileUri = fileUri,
            fileName = fileName
        )

        val version = SubmissionVersion(
            versionId = UUID.randomUUID().toString(),
            submissionId = submissionId,
            versionNumber = versionNumber,
            documentName = fileName,
            storagePath = storagePath,
            documentType = documentType,
            uploadedBy = studentId,
            uploadedAt = Timestamp.now()
        )

        versionRepository.createVersion(version)
    }
}