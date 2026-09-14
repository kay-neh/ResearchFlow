package com.researchflow.app.presentation.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Feedback
import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.domain.usecase.submission.AddFeedbackUseCase
import com.researchflow.app.domain.usecase.submission.GetSubmissionFeedbackUseCase
import com.researchflow.app.domain.usecase.submission.GetSubmissionUseCase
import com.researchflow.app.domain.usecase.submission.GetSubmissionVersionsUseCase
import com.researchflow.app.domain.usecase.submission.OpenSubmissionDocumentUseCase
import com.researchflow.app.domain.usecase.submission.UpdateSubmissionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubmissionDetailsUiState(
    val isLoading: Boolean = false,
    val submission: Submission? = null,
    val versions: List<SubmissionVersion> = emptyList(),
    val feedback: List<Feedback> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class SubmissionDetailsViewModel @Inject constructor(
    private val getSubmissionUseCase: GetSubmissionUseCase,
    private val getSubmissionVersionsUseCase: GetSubmissionVersionsUseCase,
    private val getSubmissionFeedbackUseCase: GetSubmissionFeedbackUseCase,
    private val addFeedbackUseCase: AddFeedbackUseCase,
    private val openSubmissionDocumentUseCase: OpenSubmissionDocumentUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val updateSubmissionStatusUseCase: UpdateSubmissionStatusUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SubmissionDetailsUiState())

    val uiState: StateFlow<SubmissionDetailsUiState> =
        _uiState.asStateFlow()

    fun loadSubmission(submissionId: String) {
        viewModelScope.launch {

            _uiState.value = SubmissionDetailsUiState(
                isLoading = true
            )

            try {
                val submission = getSubmissionUseCase(submissionId)

                if (submission == null) {
                    _uiState.value = SubmissionDetailsUiState(
                        errorMessage = "Submission not found"
                    )
                } else {
                    val versions = getSubmissionVersionsUseCase(
                        submissionId
                    )

                    val feedback = getSubmissionFeedbackUseCase(
                        submissionId
                    )

                    _uiState.value = SubmissionDetailsUiState(
                        submission = submission,
                        versions = versions,
                        feedback = feedback
                    )
                }

            } catch (e: Exception) {
                _uiState.value = SubmissionDetailsUiState(
                    errorMessage = e.message
                        ?: "Failed to load submission"
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
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: "Failed to open document"
                )
            }
        }
    }

    fun addFeedback(
        submissionId: String,
        versionId: String,
        comment: String
    ) {
        viewModelScope.launch {
            try {
                val supervisorId = firebaseAuth.currentUser
                    ?.uid
                    ?: throw IllegalStateException(
                        "Supervisor is not logged in"
                    )

                addFeedbackUseCase(
                    submissionId = submissionId,
                    versionId = versionId,
                    supervisorId = supervisorId,
                    comment = comment
                )

                val updatedFeedback =
                    getSubmissionFeedbackUseCase(submissionId)

                _uiState.value = _uiState.value.copy(
                    feedback = updatedFeedback,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: "Failed to add feedback"
                )
            }
        }
    }

    fun updateStatus(
        submissionId: String,
        newStatus: SubmissionStatus
    ) {
        viewModelScope.launch {
            try {
                val currentSubmission =
                    _uiState.value.submission
                        ?: throw IllegalStateException(
                            "Submission not loaded"
                        )

                updateSubmissionStatusUseCase(
                    submissionId = submissionId,
                    currentStatus = currentSubmission.status,
                    newStatus = newStatus
                )

                val updatedSubmission =
                    getSubmissionUseCase(submissionId)

                _uiState.value = _uiState.value.copy(
                    submission = updatedSubmission,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: "Failed to update submission status"
                )
            }
        }
    }

    fun loadVersionFeedback(versionId: String) {
        viewModelScope.launch {
            try {
                val versionFeedback =
                    getSubmissionFeedbackUseCase.byVersion(versionId)

                _uiState.value = _uiState.value.copy(
                    feedback = versionFeedback,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: "Failed to load version feedback"
                )
            }
        }
    }
}