package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.User
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
}