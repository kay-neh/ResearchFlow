package com.researchflow.app.domain.usecase.auth

import com.researchflow.app.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke() {
        authRepository.logout()
    }
}