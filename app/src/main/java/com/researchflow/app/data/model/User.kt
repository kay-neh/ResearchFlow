package com.researchflow.app.data.model

import com.google.firebase.Timestamp

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.STUDENT,
    val profilePictureUrl: String? = null,
    val createdAt: Timestamp? = null
)

enum class UserRole {
    STUDENT,
    SUPERVISOR,
    ADMIN
}