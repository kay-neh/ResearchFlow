package com.researchflow.app.data.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionStatus
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

    suspend fun getSubmissionsByStudent(
        studentId: String
    ): List<Submission> {
        return firestore.collection("submissions")
            .whereEqualTo("studentId", studentId)
            .get()
            .await()
            .toObjects(Submission::class.java)
    }

    suspend fun getSubmissionsBySupervisor(
        supervisorId: String
    ): List<Submission> {
        return firestore.collection("submissions")
            .whereEqualTo("supervisorId", supervisorId)
            .get()
            .await()
            .toObjects(Submission::class.java)
    }

    suspend fun updateSubmissionStatus(
        submissionId: String,
        status: SubmissionStatus
    ) {
        firestore.collection("submissions")
            .document(submissionId)
            .update(
                "status",
                status,
                "updatedAt",
                Timestamp.now()
            )
            .await()
    }

    suspend fun updateSubmissionForResubmission(
        submissionId: String,
        currentVersion: Int
    ) {
        firestore.collection("submissions")
            .document(submissionId)
            .update(
                "currentVersion",
                currentVersion,
                "status",
                SubmissionStatus.RESUBMITTED,
                "updatedAt",
                Timestamp.now()
            )
            .await()
    }
}