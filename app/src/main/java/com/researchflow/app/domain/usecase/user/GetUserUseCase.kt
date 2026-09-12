package com.researchflow.app.domain.usecase.user

import com.researchflow.app.data.model.User
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        userId: String
    ): User? {
        return userRepository.getUser(userId)
    }
}