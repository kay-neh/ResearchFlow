package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.Submission
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSubmissionService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createSubmission(submission: Submission) {
        firestore.collection("submissions")
            .document(submission.submissionId)
            .set(submission)
            .await()
    }

    suspend fun getSubmission(submissionId: String): Submission? {
        return firestore.collection("submissions")
            .document(submissionId)
            .get()
            .await()
            .toObject(Submission::class.java)
    }
}