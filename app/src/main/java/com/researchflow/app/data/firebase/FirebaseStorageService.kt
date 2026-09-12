package com.researchflow.app.data.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageService @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadSubmissionDocument(
        studentId: String,
        submissionId: String,
        versionNumber: Int,
        fileUri: Uri,
        fileName: String
    ): String {

        val fileRef = storage.reference
            .child("research-documents")
            .child(studentId)
            .child(submissionId)
            .child("version-$versionNumber")
            .child(fileName)

        fileRef.putFile(fileUri).await()

        return fileRef.downloadUrl.await().toString()
    }
}