package com.researchflow.app.domain.usecase.auth

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.researchflow.app.data.model.User
import com.researchflow.app.data.model.UserRole
import com.researchflow.app.domain.repository.AuthRepository
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): FirebaseUser {

        val firebaseUser = authRepository.register(
            email = email,
            password = password
        )

        val user = User(
            userId = firebaseUser.uid,
            name = name,
            email = email,
            role = UserRole.STUDENT,
            createdAt = Timestamp.now()
        )

        userRepository.createUser(user)

        return firebaseUser
    }
}