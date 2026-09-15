package com.researchflow.app.data.supabase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
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

    suspend fun uploadProfilePicture(
        userId: String,
        fileUri: Uri
    ): String = withContext(Dispatchers.IO) {

        val inputStream = context.contentResolver.openInputStream(fileUri)
            ?: throw IllegalArgumentException("Unable to read selected image")

        val originalBitmap = inputStream.use {
            BitmapFactory.decodeStream(it)
        } ?: throw IllegalArgumentException("Unable to process selected image")

        val maxDimension = 1080

        val scale = minOf(
            1f,
            maxDimension.toFloat() /
                    maxOf(originalBitmap.width, originalBitmap.height)
        )

        val resizedBitmap = if (scale < 1f) {
            Bitmap.createScaledBitmap(
                originalBitmap,
                (originalBitmap.width * scale).toInt(),
                (originalBitmap.height * scale).toInt(),
                true
            )
        } else {
            originalBitmap
        }

        val outputStream = ByteArrayOutputStream()

        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            85,
            outputStream
        )

        val fileBytes = outputStream.toByteArray()

        if (resizedBitmap !== originalBitmap) {
            resizedBitmap.recycle()
        }

        originalBitmap.recycle()

        val storagePath = "$userId/profile.jpg"

        supabase.storage
            .from("profile-pictures")
            .upload(
                path = storagePath,
                data = fileBytes
            ) {
                upsert = true
            }

        val publicUrl = supabase.storage
            .from("profile-pictures")
            .publicUrl(storagePath)

        "$publicUrl?v=${System.currentTimeMillis()}"
    }

}