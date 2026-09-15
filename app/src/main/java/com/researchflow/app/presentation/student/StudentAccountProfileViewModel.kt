package com.researchflow.app.presentation.student

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.domain.model.StudentAccountProfile
import com.researchflow.app.domain.usecase.student.GetStudentAccountProfileUseCase
import com.researchflow.app.domain.usecase.user.UploadProfilePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentAccountProfileUiState(
    val isLoading: Boolean = false,
    val profile: StudentAccountProfile? = null,
    val isUploadingPicture: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StudentAccountProfileViewModel @Inject constructor(
    private val getStudentAccountProfileUseCase: GetStudentAccountProfileUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentAccountProfileUiState())
    val uiState: StateFlow<StudentAccountProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId == null) {
            _uiState.value = StudentAccountProfileUiState(
                error = "User is not logged in"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val profile = getStudentAccountProfileUseCase(userId)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profile = profile,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load profile"
                )
            }
        }
    }

    fun uploadProfilePicture(fileUri: Uri) {
        val userId = firebaseAuth.currentUser?.uid

        if (userId == null) {
            _uiState.value = _uiState.value.copy(
                error = "User is not logged in"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isUploadingPicture = true,
                error = null
            )

            try {
                uploadProfilePictureUseCase(
                    userId = userId,
                    fileUri = fileUri
                )

                val updatedProfile =
                    getStudentAccountProfileUseCase(userId)

                _uiState.value = _uiState.value.copy(
                    isUploadingPicture = false,
                    profile = updatedProfile,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPicture = false,
                    error = e.message ?: "Failed to upload profile picture"
                )
            }
        }
    }
}