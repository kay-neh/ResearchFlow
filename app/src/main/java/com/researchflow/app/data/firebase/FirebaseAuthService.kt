package com.researchflow.app.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    suspend fun login(
        email: String,
        password: String
    ): FirebaseUser {
        return firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
            .user
            ?: throw IllegalStateException("Login failed")
    }

    suspend fun register(
        email: String,
        password: String
    ): FirebaseUser {
        return firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()
            .user
            ?: throw IllegalStateException("Registration failed")
    }

    suspend fun resetPassword(email: String) {
        firebaseAuth
            .sendPasswordResetEmail(email)
            .await()
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}