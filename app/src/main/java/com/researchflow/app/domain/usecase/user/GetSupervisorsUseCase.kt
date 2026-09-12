package com.researchflow.app.domain.usecase.user

import com.researchflow.app.data.model.User
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class GetSupervisorsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return userRepository.getSupervisors()
    }
}