package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.User

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUser(userId: String): User?

    suspend fun getSupervisors(): List<User>
}