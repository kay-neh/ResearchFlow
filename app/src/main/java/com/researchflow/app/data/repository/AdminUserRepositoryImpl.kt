package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.SecondaryFirebaseAuthService
import com.researchflow.app.domain.repository.AdminUserRepository
import javax.inject.Inject

class AdminUserRepositoryImpl @Inject constructor(
    private val secondaryFirebaseAuthService: SecondaryFirebaseAuthService
) : AdminUserRepository {

    override suspend fun createUserAccount(
        email: String,
        password: String
    ): String {
        return secondaryFirebaseAuthService.createUser(
            email = email,
            password = password
        )
    }
}