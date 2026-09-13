package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.UserRole
import com.researchflow.app.domain.usecase.user.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminCreateUserUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminCreateUserViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminCreateUserUiState()
    )

    val uiState: StateFlow<AdminCreateUserUiState> =
        _uiState.asStateFlow()

    fun createUser(
        name: String,
        email: String,
        password: String,
        role: UserRole,
        matricNumber: String = "",
        department: String = ""
    ) {
        viewModelScope.launch {
            _uiState.value = AdminCreateUserUiState(
                isLoading = true
            )

            try {
                createUserUseCase(
                    name = name,
                    email = email,
                    password = password,
                    role = role,
                    matricNumber = matricNumber,
                    department = department
                )

                _uiState.value = AdminCreateUserUiState(
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = AdminCreateUserUiState(
                    errorMessage = e.message
                        ?: "Failed to create user"
                )
            }
        }
    }

    fun clearState() {
        _uiState.value = AdminCreateUserUiState()
    }
}