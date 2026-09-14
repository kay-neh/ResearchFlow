package com.researchflow.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.researchflow.app.data.model.ResearchArchive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreResearchArchiveService @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createArchive(archive: ResearchArchive) {
        firestore.collection("research_archives")
            .document(archive.archiveId)
            .set(archive)
            .await()
    }

    suspend fun getArchive(
        archiveId: String
    ): ResearchArchive? {
        return firestore.collection("research_archives")
            .document(archiveId)
            .get()
            .await()
            .toObject(ResearchArchive::class.java)
    }

    suspend fun getAllArchives(): List<ResearchArchive> {
        return firestore.collection("research_archives")
            .orderBy(
                "archivedAt",
                com.google.firebase.firestore.Query.Direction.DESCENDING
            )
            .get()
            .await()
            .toObjects(ResearchArchive::class.java)
    }

    suspend fun getArchivesByStudent(
        studentId: String
    ): List<ResearchArchive> {
        return firestore.collection("research_archives")
            .whereEqualTo("studentId", studentId)
            .get()
            .await()
            .toObjects(ResearchArchive::class.java)
    }
}