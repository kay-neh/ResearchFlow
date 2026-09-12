package com.researchflow.app.presentation.student

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.DocumentType
import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.domain.usecase.submission.CreateSubmissionUseCase
import com.researchflow.app.domain.usecase.submission.UploadSubmissionVersionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class SubmissionUiState(
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class SubmissionViewModel @Inject constructor(
    private val createSubmissionUseCase: CreateSubmissionUseCase,
    private val uploadSubmissionVersionUseCase: UploadSubmissionVersionUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmissionUiState())
    val uiState: StateFlow<SubmissionUiState> = _uiState.asStateFlow()

    fun submitResearch(
        title: String,
        fileUri: Uri,
        fileName: String,
        documentType: DocumentType
    ) {
        val studentId = firebaseAuth.currentUser?.uid

        if (studentId == null) {
            _uiState.value = SubmissionUiState(
                errorMessage = "User is not authenticated"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = SubmissionUiState(isLoading = true)

            try {
                val submissionId = UUID.randomUUID().toString()

                val now = Timestamp.now()

                val submission = Submission(
                    submissionId = submissionId,
                    studentId = studentId,
                    title = title,
                    status = SubmissionStatus.SUBMITTED,
                    currentVersion = 1,
                    createdAt = now,
                    updatedAt = now
                )

                createSubmissionUseCase(submission)

                uploadSubmissionVersionUseCase(
                    studentId = studentId,
                    submissionId = submissionId,
                    versionNumber = 1,
                    fileUri = fileUri,
                    fileName = fileName,
                    documentType = documentType
                )

                _uiState.value = SubmissionUiState(
                    isSubmitted = true
                )

            } catch (e: Exception) {
                _uiState.value = SubmissionUiState(
                    errorMessage = e.message ?: "Submission failed"
                )
            }
        }
    }
}