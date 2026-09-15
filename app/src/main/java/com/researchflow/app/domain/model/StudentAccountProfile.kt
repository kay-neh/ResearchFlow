package com.researchflow.app.domain.model

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.UserRole

data class StudentAccountProfile(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.STUDENT,
    val profilePictureUrl: String? = null,
    val isActive: Boolean = true,
    val createdAt: Timestamp? = null,
    val matricNumber: String = "",
    val department: String = "",
    val supervisorId: String? = null,
    val supervisorName: String? = null
)