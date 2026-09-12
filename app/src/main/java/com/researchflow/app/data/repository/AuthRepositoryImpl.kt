package com.researchflow.app.data.repository

import com.google.firebase.auth.FirebaseUser
import com.researchflow.app.data.firebase.FirebaseAuthService
import com.researchflow.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: FirebaseAuthService
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = authService.currentUser

    override suspend fun login(
        email: String,
        password: String
    ): FirebaseUser {
        return authService.login(email, password)
    }

    override suspend fun register(
        email: String,
        password: String
    ): FirebaseUser {
        return authService.register(email, password)
    }

    override suspend fun resetPassword(
        email: String
    ) {
        authService.resetPassword(email)
    }

    override fun logout() {
        authService.logout()
    }
}