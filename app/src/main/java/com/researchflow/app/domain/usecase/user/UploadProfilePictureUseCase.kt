package com.researchflow.app.domain.usecase.user

import android.content.Context
import android.net.Uri
import androidx.core.content.ContentResolverCompat
import androidx.core.net.toUri
import com.researchflow.app.data.supabase.SupabaseStorageService
import com.researchflow.app.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UploadProfilePictureUseCase @Inject constructor(
    private val supabaseStorageService: SupabaseStorageService,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) {

    private companion object {
        const val MAX_IMAGE_SIZE = 5 * 1024 * 1024 // 5 MB
    }

    suspend operator fun invoke(
        userId: String,
        fileUri: Uri
    ): String {

        validateImage(fileUri)

        val profilePictureUrl =
            supabaseStorageService.uploadProfilePicture(
                userId = userId,
                fileUri = fileUri
            )

        userRepository.updateProfilePicture(
            userId = userId,
            profilePictureUrl = profilePictureUrl
        )

        return profilePictureUrl
    }

    private fun validateImage(fileUri: Uri) {
        val mimeType = context.contentResolver.getType(fileUri)

        val allowedTypes = setOf(
            "image/jpeg",
            "image/png"
        )

        if (mimeType !in allowedTypes) {
            throw IllegalArgumentException(
                "Please select a JPG, JPEG, or PNG image"
            )
        }

        val fileSize = context.contentResolver
            .openFileDescriptor(fileUri, "r")
            ?.use { it.statSize }
            ?: -1L

        if (fileSize > MAX_IMAGE_SIZE) {
            throw IllegalArgumentException(
                "Profile picture must be 5 MB or smaller"
            )
        }
    }
}