package com.researchflow.app.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.usecase.submission.GetStudentSubmissionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubmissionStatusUiState(
    val isLoading: Boolean = false,
    val submissions: List<Submission> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class SubmissionStatusViewModel @Inject constructor(
    private val getStudentSubmissionsUseCase: GetStudentSubmissionsUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SubmissionStatusUiState()
    )

    val uiState: StateFlow<SubmissionStatusUiState> =
        _uiState.asStateFlow()

    fun loadSubmissions() {
        val studentId = firebaseAuth.currentUser?.uid

        if (studentId == null) {
            _uiState.value = SubmissionStatusUiState(
                errorMessage = "User is not authenticated"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = SubmissionStatusUiState(
                isLoading = true
            )

            try {
                val submissions =
                    getStudentSubmissionsUseCase(studentId)

                _uiState.value = SubmissionStatusUiState(
                    submissions = submissions
                )

            } catch (e: Exception) {
                _uiState.value = SubmissionStatusUiState(
                    errorMessage =
                        e.message ?: "Failed to load submissions"
                )
            }
        }
    }
}