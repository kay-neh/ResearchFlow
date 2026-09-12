package com.researchflow.app.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.researchflow.app.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): FirebaseUser {
        return authRepository.register(
            email = email,
            password = password
        )
    }
}