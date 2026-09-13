package com.researchflow.app.domain.usecase.user

import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class SetUserActiveUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        isActive: Boolean
    ) {
        userRepository.setUserActive(
            userId = userId,
            isActive = isActive
        )
    }
}