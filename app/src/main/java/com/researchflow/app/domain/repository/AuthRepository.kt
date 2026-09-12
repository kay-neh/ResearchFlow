package com.researchflow.app.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    val currentUser: FirebaseUser?

    suspend fun login(
        email: String,
        password: String
    ): FirebaseUser

    suspend fun register(
        email: String,
        password: String
    ): FirebaseUser

    suspend fun resetPassword(
        email: String
    )

    fun logout()
}