package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.SubmissionVersion
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSubmissionVersionService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createVersion(version: SubmissionVersion) {
        firestore.collection("submission_versions")
            .document(version.versionId)
            .set(version)
            .await()
    }

    suspend fun getVersion(versionId: String): SubmissionVersion? {
        return firestore.collection("submission_versions")
            .document(versionId)
            .get()
            .await()
            .toObject(SubmissionVersion::class.java)
    }

    suspend fun getVersionsBySubmission(
        submissionId: String
    ): List<SubmissionVersion> {
        return firestore.collection("submission_versions")
            .whereEqualTo("submissionId", submissionId)
            .get()
            .await()
            .toObjects(SubmissionVersion::class.java)
    }
}