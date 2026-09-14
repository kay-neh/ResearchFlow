package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.researchflow.app.data.model.Feedback
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreFeedbackService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createFeedback(feedback: Feedback) {
        firestore
            .collection("feedback")
            .document(feedback.feedbackId)
            .set(feedback)
            .await()
    }

    suspend fun getFeedback(feedbackId: String): Feedback? =
        firestore
            .collection("feedback")
            .document(feedbackId)
            .get()
            .await()
            .toObject<Feedback>()

    suspend fun getFeedbackBySubmission(
        submissionId: String
    ): List<Feedback> =
        firestore
            .collection("feedback")
            .whereEqualTo("submissionId", submissionId)
            .get()
            .await()
            .toObjects<Feedback>()

    suspend fun getFeedbackByVersion(
        versionId: String
    ): List<Feedback> =
        firestore
            .collection("feedback")
            .whereEqualTo("versionId", versionId)
            .get()
            .await()
            .toObjects<Feedback>()
}