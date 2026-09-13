package com.researchflow.app.domain.repository

interface AdminUserRepository {

    suspend fun createUserAccount(
        email: String,
        password: String
    ): String
}