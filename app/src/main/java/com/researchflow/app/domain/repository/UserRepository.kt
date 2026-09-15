package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUser(userId: String): User?

    suspend fun getSupervisors(): List<User>

    suspend fun getAllUsers(): List<User>

    suspend fun updateUser(
        userId: String,
        name: String
    )

    suspend fun setUserActive(
        userId: String,
        isActive: Boolean
    )

    suspend fun updateProfilePicture(
        userId: String,
        profilePictureUrl: String
    )
}