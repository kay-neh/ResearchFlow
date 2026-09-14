package com.researchflow.app.data.supabase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import javax.inject.Inject

class SupabaseStorageService @Inject constructor(
    private val supabase: SupabaseClient,
    @ApplicationContext private val context: Context
) {

    suspend fun uploadSubmissionDocument(
        studentId: String,
        submissionId: String,
        versionNumber: Int,
        fileUri: Uri,
        fileName: String
    ): String = withContext(Dispatchers.IO) {

        val inputStream = context.contentResolver.openInputStream(fileUri)
            ?: throw IllegalArgumentException("Unable to read selected file")

        val fileBytes = inputStream.use { it.readBytes() }

        val storagePath =
            "$studentId/$submissionId/version-$versionNumber/$fileName"

        supabase.storage
            .from("research-documents")
            .upload(
                path = storagePath,
                data = fileBytes
            )

        storagePath
    }

    suspend fun createDocumentSignedUrl(
        storagePath: String
    ): String {
        return supabase.storage
            .from("research-documents")
            .createSignedUrl(
                path = storagePath,
                expiresIn = 60.minutes
            )
    }


}