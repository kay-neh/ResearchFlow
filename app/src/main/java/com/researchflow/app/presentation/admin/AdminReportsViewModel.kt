package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.AdminReport
import com.researchflow.app.domain.usecase.admin.GetAdminReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminReportsUiState(
    val isLoading: Boolean = false,
    val report: AdminReport? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminReportsViewModel @Inject constructor(
    private val getAdminReportUseCase: GetAdminReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminReportsUiState()
    )

    val uiState: StateFlow<AdminReportsUiState> =
        _uiState.asStateFlow()

    fun loadReport() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val report = getAdminReportUseCase()

                _uiState.value = AdminReportsUiState(
                    isLoading = false,
                    report = report
                )
            } catch (e: Exception) {
                _uiState.value = AdminReportsUiState(
                    isLoading = false,
                    errorMessage = e.message
                        ?: "Failed to generate report"
                )
            }
        }
    }

    fun refresh() {
        loadReport()
    }
}