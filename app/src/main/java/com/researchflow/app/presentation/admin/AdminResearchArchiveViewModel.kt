package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.ResearchArchive
import com.researchflow.app.domain.usecase.archive.GetResearchArchivesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResearchArchiveUiState(
    val isLoading: Boolean = false,
    val archives: List<ResearchArchive> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class AdminResearchArchiveViewModel @Inject constructor(
    private val getResearchArchivesUseCase: GetResearchArchivesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ResearchArchiveUiState()
    )

    val uiState: StateFlow<ResearchArchiveUiState> =
        _uiState.asStateFlow()

    fun loadArchives() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val archives = getResearchArchivesUseCase()

                _uiState.value = ResearchArchiveUiState(
                    isLoading = false,
                    archives = archives
                )
            } catch (e: Exception) {
                _uiState.value = ResearchArchiveUiState(
                    isLoading = false,
                    errorMessage = e.message
                        ?: "Failed to load research archives"
                )
            }
        }
    }

    fun refresh() {
        loadArchives()
    }
}