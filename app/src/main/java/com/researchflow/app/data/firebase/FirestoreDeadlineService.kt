package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.Deadline
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDeadlineService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createDeadline(deadline: Deadline) {
        firestore.collection("deadlines")
            .document(deadline.deadlineId)
            .set(deadline)
            .await()
    }

    suspend fun getDeadline(deadlineId: String): Deadline? {
        return firestore.collection("deadlines")
            .document(deadlineId)
            .get()
            .await()
            .toObject(Deadline::class.java)
    }

    suspend fun getDeadlineBySubmission(
        submissionId: String
    ): Deadline? {
        return firestore.collection("deadlines")
            .whereEqualTo("submissionId", submissionId)
            .whereEqualTo("isActive", true)
            .limit(1)
            .get()
            .await()
            .toObjects(Deadline::class.java)
            .firstOrNull()
    }

    suspend fun getDeadlinesBySupervisor(
        supervisorId: String
    ): List<Deadline> {
        return firestore.collection("deadlines")
            .whereEqualTo("supervisorId", supervisorId)
            .get()
            .await()
            .toObjects(Deadline::class.java)
    }

    suspend fun updateDeadline(
        deadlineId: String,
        title: String,
        description: String,
        deadlineDate: com.google.firebase.Timestamp
    ) {
        firestore.collection("deadlines")
            .document(deadlineId)
            .update(
                "title", title,
                "description", description,
                "deadlineDate", deadlineDate
            )
            .await()
    }

    suspend fun setDeadlineActive(
        deadlineId: String,
        isActive: Boolean
    ) {
        firestore.collection("deadlines")
            .document(deadlineId)
            .update("isActive", isActive)
            .await()
    }
}