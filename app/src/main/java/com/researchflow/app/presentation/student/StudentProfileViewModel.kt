package com.researchflow.app.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Student
import com.researchflow.app.domain.usecase.student.CreateStudentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentProfileUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val createStudentUseCase: CreateStudentUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentProfileUiState())
    val uiState: StateFlow<StudentProfileUiState> = _uiState.asStateFlow()

    fun saveProfile(
        matricNumber: String,
        department: String
    ) {
        val userId = firebaseAuth.currentUser?.uid

        if (userId == null) {
            _uiState.value = StudentProfileUiState(
                errorMessage = "User is not authenticated"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = StudentProfileUiState(isLoading = true)

            try {
                val student = Student(
                    studentId = userId,
                    userId = userId,
                    matricNumber = matricNumber,
                    department = department
                )

                createStudentUseCase(student)

                _uiState.value = StudentProfileUiState(
                    isSaved = true
                )

            } catch (e: Exception) {
                _uiState.value = StudentProfileUiState(
                    errorMessage = e.message ?: "Failed to save profile"
                )
            }
        }
    }
}