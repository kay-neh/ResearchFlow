package com.researchflow.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.domain.usecase.auth.LoginUseCase
import com.researchflow.app.domain.usecase.auth.LogoutUseCase
import com.researchflow.app.domain.usecase.auth.RegisterUserUseCase
import com.researchflow.app.domain.usecase.auth.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                loginUseCase(email, password)

                _uiState.value = AuthUiState(
                    isAuthenticated = true
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage = e.message ?: "Login failed"
                )
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                registerUserUseCase(
                    name = name,
                    email = email,
                    password = password
                )

                _uiState.value = AuthUiState(
                    isAuthenticated = true
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage = e.message ?: "Registration failed"
                )
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)

            try {
                resetPasswordUseCase(email)

                _uiState.value = AuthUiState(
                    successMessage = "Password reset email sent"
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage = e.message ?: "Password reset failed"
                )
            }
        }
    }

    fun logout() {
        logoutUseCase()

        _uiState.value = AuthUiState(
            isAuthenticated = false
        )
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}