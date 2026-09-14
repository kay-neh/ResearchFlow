package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class ResearchArchive(
    val archiveId: String = "",
    val submissionId: String = "",
    val studentId: String = "",
    val supervisorId: String = "",
    val title: String = "",
    val documentName: String = "",
    val storagePath: String = "",
    val approvedVersion: Int = 1,
    val archivedAt: Timestamp? = null,
    val archivedBy: String = ""
)