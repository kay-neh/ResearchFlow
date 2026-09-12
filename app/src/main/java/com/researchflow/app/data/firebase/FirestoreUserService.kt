package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.User
import com.researchflow.app.data.model.UserRole
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createUser(user: User) {
        firestore.collection("users")
            .document(user.userId)
            .set(user)
            .await()
    }

    suspend fun getUser(userId: String): User? {
        return firestore.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
    }

    suspend fun getSupervisors(): List<User> {
        return firestore.collection("users")
            .whereEqualTo("role", UserRole.SUPERVISOR)
            .get()
            .await()
            .toObjects(User::class.java)
    }
}