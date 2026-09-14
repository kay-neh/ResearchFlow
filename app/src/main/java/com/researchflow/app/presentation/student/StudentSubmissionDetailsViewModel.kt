package com.researchflow.app.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Feedback
import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.domain.usecase.submission.GetSubmissionFeedbackUseCase
import com.researchflow.app.domain.usecase.submission.GetSubmissionUseCase
import com.researchflow.app.domain.usecase.submission.GetSubmissionVersionsUseCase
import com.researchflow.app.domain.usecase.submission.OpenSubmissionDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentSubmissionDetailsUiState(
    val isLoading: Boolean = false,
    val submission: Submission? = null,
    val versions: List<SubmissionVersion> = emptyList(),
    val feedback: List<Feedback> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class StudentSubmissionDetailsViewModel @Inject constructor(
    private val getSubmissionUseCase: GetSubmissionUseCase,
    private val getSubmissionVersionsUseCase: GetSubmissionVersionsUseCase,
    private val getSubmissionFeedbackUseCase: GetSubmissionFeedbackUseCase,
    private val openSubmissionDocumentUseCase: OpenSubmissionDocumentUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(StudentSubmissionDetailsUiState())

    val uiState: StateFlow<StudentSubmissionDetailsUiState> =
        _uiState.asStateFlow()

    fun loadSubmission(submissionId: String) {
        viewModelScope.launch {
            _uiState.value =
                StudentSubmissionDetailsUiState(isLoading = true)

            try {
                val submission =
                    getSubmissionUseCase(submissionId)
                        ?: throw IllegalStateException(
                            "Submission not found"
                        )

                val currentStudentId =
                    firebaseAuth.currentUser?.uid
                        ?: throw IllegalStateException(
                            "User is not authenticated"
                        )

                if (submission.studentId != currentStudentId) {
                    throw IllegalStateException(
                        "You are not allowed to view this submission."
                    )
                }

                val versions =
                    getSubmissionVersionsUseCase(submissionId)

                val feedback =
                    getSubmissionFeedbackUseCase(submissionId)

                _uiState.value =
                    StudentSubmissionDetailsUiState(
                        submission = submission,
                        versions = versions,
                        feedback = feedback
                    )

            } catch (e: Exception) {
                _uiState.value =
                    StudentSubmissionDetailsUiState(
                        errorMessage =
                            e.message ?: "Failed to load submission"
                    )
            }
        }
    }

    fun openDocument(
        storagePath: String,
        onUrlReady: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val signedUrl =
                    openSubmissionDocumentUseCase(storagePath)

                onUrlReady(signedUrl)

            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        errorMessage =
                            e.message ?: "Failed to open document"
                    )
            }
        }
    }
}