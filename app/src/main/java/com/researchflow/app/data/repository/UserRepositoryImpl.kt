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
}