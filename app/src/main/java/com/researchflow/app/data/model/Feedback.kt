package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class Feedback(
    val feedbackId: String = "",
    val submissionId: String = "",
    val versionId: String = "",
    val supervisorId: String = "",
    val comment: String = "",
    val createdAt: Timestamp? = null
)