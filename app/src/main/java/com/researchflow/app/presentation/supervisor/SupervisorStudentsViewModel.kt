package com.researchflow.app.presentation.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Student
import com.researchflow.app.domain.usecase.student.GetAssignedStudentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SupervisorStudentsUiState(
    val isLoading: Boolean = false,
    val students: List<Student> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class SupervisorStudentsViewModel @Inject constructor(
    private val getAssignedStudentsUseCase: GetAssignedStudentsUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SupervisorStudentsUiState()
    )

    val uiState: StateFlow<SupervisorStudentsUiState> =
        _uiState.asStateFlow()

    fun loadAssignedStudents() {
        val supervisorId = firebaseAuth.currentUser?.uid

        if (supervisorId == null) {
            _uiState.value = SupervisorStudentsUiState(
                errorMessage = "User is not authenticated"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = SupervisorStudentsUiState(
                isLoading = true
            )

            try {
                val students =
                    getAssignedStudentsUseCase(supervisorId)

                _uiState.value = SupervisorStudentsUiState(
                    students = students
                )

            } catch (e: Exception) {
                _uiState.value = SupervisorStudentsUiState(
                    errorMessage =
                        e.message ?: "Failed to load assigned students"
                )
            }
        }
    }
}