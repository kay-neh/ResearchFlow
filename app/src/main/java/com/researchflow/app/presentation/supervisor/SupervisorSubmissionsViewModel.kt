package com.researchflow.app.presentation.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.usecase.submission.GetSupervisorSubmissionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SupervisorSubmissionsUiState(
    val isLoading: Boolean = false,
    val submissions: List<Submission> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class SupervisorSubmissionsViewModel @Inject constructor(
    private val getSupervisorSubmissionsUseCase: GetSupervisorSubmissionsUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SupervisorSubmissionsUiState()
    )

    val uiState: StateFlow<SupervisorSubmissionsUiState> =
        _uiState.asStateFlow()

    fun loadSubmissions() {
        viewModelScope.launch {
            _uiState.value = SupervisorSubmissionsUiState(
                isLoading = true
            )

            try {
                val supervisorId = firebaseAuth.currentUser?.uid
                    ?: throw IllegalStateException(
                        "Supervisor is not logged in"
                    )

                val submissions =
                    getSupervisorSubmissionsUseCase(supervisorId)

                _uiState.value = SupervisorSubmissionsUiState(
                    submissions = submissions
                )

            } catch (e: Exception) {
                _uiState.value = SupervisorSubmissionsUiState(
                    errorMessage = e.message
                        ?: "Failed to load submissions"
                )
            }
        }
    }
}