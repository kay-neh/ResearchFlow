package com.researchflow.app.domain.usecase.user

import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
        name: String
    ) {
        userRepository.updateUser(
            userId = userId,
            name = name
        )
    }
}