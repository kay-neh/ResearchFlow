package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.Student
import com.researchflow.app.data.model.User
import com.researchflow.app.domain.usecase.student.GetAllStudentsUseCase
import com.researchflow.app.domain.usecase.user.GetSupervisorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminAssignmentUiState(
    val isLoading: Boolean = false,
    val students: List<Student> = emptyList(),
    val supervisors: List<User> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class AdminAssignmentViewModel @Inject constructor(
    private val getAllStudentsUseCase: GetAllStudentsUseCase,
    private val getSupervisorsUseCase: GetSupervisorsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminAssignmentUiState()
    )

    val uiState: StateFlow<AdminAssignmentUiState> =
        _uiState.asStateFlow()

    fun loadAssignmentData() {
        viewModelScope.launch {
            _uiState.value = AdminAssignmentUiState(
                isLoading = true
            )

            try {
                val students = getAllStudentsUseCase()
                val supervisors = getSupervisorsUseCase()

                _uiState.value = AdminAssignmentUiState(
                    students = students,
                    supervisors = supervisors
                )

            } catch (e: Exception) {
                _uiState.value = AdminAssignmentUiState(
                    errorMessage =
                        e.message ?: "Failed to load assignment data"
                )
            }
        }
    }
}