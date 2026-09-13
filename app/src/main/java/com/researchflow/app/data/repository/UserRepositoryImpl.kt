package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreUserService
import com.researchflow.app.data.model.User
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestoreUserService: FirestoreUserService
) : UserRepository {

    override suspend fun createUser(user: User) {
        firestoreUserService.createUser(user)
    }

    override suspend fun getUser(userId: String): User? {
        return firestoreUserService.getUser(userId)
    }

    override suspend fun getSupervisors(): List<User> {
        return firestoreUserService.getSupervisors()
    }

    override suspend fun getAllUsers(): List<User> {
        return firestoreUserService.getAllUsers()
    }

    override suspend fun updateUser(
        userId: String,
        name: String
    ) {
        firestoreUserService.updateUser(
            userId = userId,
            name = name
        )
    }

    override suspend fun setUserActive(
        userId: String,
        isActive: Boolean
    ) {
        firestoreUserService.setUserActive(
            userId = userId,
            isActive = isActive
        )
    }
}