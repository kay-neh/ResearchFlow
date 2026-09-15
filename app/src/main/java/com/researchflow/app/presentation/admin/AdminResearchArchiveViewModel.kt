package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.ResearchArchive
import com.researchflow.app.domain.usecase.archive.GetResearchArchivesUseCase
import com.researchflow.app.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResearchArchiveUiState(
    val isLoading: Boolean = false,
    val archives: List<ResearchArchive> = emptyList(),
    val studentNames: Map<String, String> = emptyMap(),
    val supervisorNames: Map<String, String> = emptyMap(),
    val errorMessage: String? = null
)

@HiltViewModel
class AdminResearchArchiveViewModel @Inject constructor(
    private val getResearchArchivesUseCase: GetResearchArchivesUseCase,
    private val getUserUseCase: GetUserUseCase
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

                // Get unique student and supervisor IDs
                val studentIds = archives
                    .map { it.studentId }
                    .filter { it.isNotBlank() }
                    .distinct()

                val supervisorIds = archives
                    .map { it.supervisorId }
                    .filter { it.isNotBlank() }
                    .distinct()

                // Load users concurrently
                val studentResults = studentIds.map { userId ->
                    async {
                        userId to getUserUseCase(userId)
                    }
                }.awaitAll()

                val supervisorResults = supervisorIds.map { userId ->
                    async {
                        userId to getUserUseCase(userId)
                    }
                }.awaitAll()

                val studentNames = studentResults
                    .mapNotNull { (userId, user) ->
                        user?.name?.takeIf { it.isNotBlank() }
                            ?.let { userId to it }
                    }
                    .toMap()

                val supervisorNames = supervisorResults
                    .mapNotNull { (userId, user) ->
                        user?.name?.takeIf { it.isNotBlank() }
                            ?.let { userId to it }
                    }
                    .toMap()

                _uiState.value = ResearchArchiveUiState(
                    isLoading = false,
                    archives = archives,
                    studentNames = studentNames,
                    supervisorNames = supervisorNames
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

    fun getStudentName(studentId: String): String {
        return _uiState.value.studentNames[studentId]
            ?: "Student not available"
    }

    fun getSupervisorName(supervisorId: String): String {
        return _uiState.value.supervisorNames[supervisorId]
            ?: "Supervisor not available"
    }

    fun refresh() {
        loadArchives()
    }
}
