package com.researchflow.app.presentation.supervisor

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.User
import com.researchflow.app.domain.usecase.user.GetUserUseCase
import com.researchflow.app.domain.usecase.user.UploadProfilePictureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SupervisorAccountProfileUiState(
    val isLoading: Boolean = false,
    val profile: User? = null,
    val isUploadingPicture: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SupervisorAccountProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val uploadProfilePictureUseCase: UploadProfilePictureUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SupervisorAccountProfileUiState()
    )

    val uiState: StateFlow<SupervisorAccountProfileUiState> =
        _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId == null) {
            _uiState.value = SupervisorAccountProfileUiState(
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
                val user = getUserUseCase(userId)

                if (user == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "User profile not found"
                    )
                    return@launch
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profile = user,
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

                val updatedUser = getUserUseCase(userId)

                _uiState.value = _uiState.value.copy(
                    isUploadingPicture = false,
                    profile = updatedUser,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUploadingPicture = false,
                    error = e.message
                        ?: "Failed to upload profile picture"
                )
            }
        }
    }
}