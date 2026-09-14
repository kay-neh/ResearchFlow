package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class SubmissionVersion(
    val versionId: String = "",
    val submissionId: String = "",
    val versionNumber: Int = 1,
    val documentName: String = "",
    val storagePath: String = "",
    val documentType: DocumentType = DocumentType.PDF,
    val uploadedBy: String = "",
    val uploadedAt: Timestamp? = null
)

enum class DocumentType {
    PDF,
    DOCX
}