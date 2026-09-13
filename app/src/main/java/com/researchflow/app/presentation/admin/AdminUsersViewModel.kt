package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.User
import com.researchflow.app.domain.usecase.user.GetAllUsersUseCase
import com.researchflow.app.domain.usecase.user.SetUserActiveUseCase
import com.researchflow.app.domain.usecase.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminUsersUiState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class AdminUsersViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val setUserActiveUseCase: SetUserActiveUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUsersUiState())
    val uiState: StateFlow<AdminUsersUiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = AdminUsersUiState(
                isLoading = true
            )

            try {
                val users = getAllUsersUseCase()

                _uiState.value = AdminUsersUiState(
                    users = users
                )
            } catch (e: Exception) {
                _uiState.value = AdminUsersUiState(
                    errorMessage = e.message
                        ?: "Failed to load users"
                )
            }
        }
    }

    fun updateUser(
        userId: String,
        name: String
    ) {
        viewModelScope.launch {
            try {
                updateUserUseCase(
                    userId = userId,
                    name = name
                )

                val users = getAllUsersUseCase()

                _uiState.value = AdminUsersUiState(
                    users = users
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: "Failed to update user"
                )
            }
        }
    }

    fun setUserActive(
        userId: String,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            try {
                setUserActiveUseCase(
                    userId = userId,
                    isActive = isActive
                )

                val users = getAllUsersUseCase()

                _uiState.value = AdminUsersUiState(
                    users = users
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                        ?: "Failed to update user status"
                )
            }
        }
    }
}