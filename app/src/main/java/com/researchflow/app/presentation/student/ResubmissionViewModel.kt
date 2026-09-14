package com.researchflow.app.presentation.student

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.DocumentType
import com.researchflow.app.domain.usecase.submission.ResubmitSubmissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResubmissionUiState(
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ResubmissionViewModel @Inject constructor(
    private val resubmitSubmissionUseCase: ResubmitSubmissionUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ResubmissionUiState())

    val uiState: StateFlow<ResubmissionUiState> =
        _uiState.asStateFlow()

    fun resubmit(
        submissionId: String,
        currentVersion: Int,
        fileUri: Uri,
        fileName: String,
        documentType: DocumentType
    ) {
        val studentId = firebaseAuth.currentUser?.uid

        if (studentId == null) {
            _uiState.value = ResubmissionUiState(
                errorMessage = "User is not authenticated"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = ResubmissionUiState(
                isLoading = true
            )

            try {
                resubmitSubmissionUseCase(
                    submissionId = submissionId,
                    studentId = studentId,
                    currentVersion = currentVersion,
                    fileUri = fileUri,
                    fileName = fileName,
                    documentType = documentType
                )

                _uiState.value = ResubmissionUiState(
                    isSubmitted = true
                )

            } catch (e: Exception) {
                _uiState.value = ResubmissionUiState(
                    errorMessage = e.message
                        ?: "Resubmission failed"
                )
            }
        }
    }
}