package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class Deadline(
    val deadlineId: String = "",
    val submissionId: String = "",
    val supervisorId: String = "",
    val title: String = "",
    val description: String = "",
    val deadlineDate: Timestamp? = null,
    val createdAt: Timestamp? = null,
    val isActive: Boolean = true
)