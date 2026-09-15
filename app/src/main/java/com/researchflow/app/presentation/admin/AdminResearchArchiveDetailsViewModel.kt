package com.researchflow.app.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.researchflow.app.data.model.ResearchArchive
import com.researchflow.app.data.model.User
import com.researchflow.app.domain.usecase.archive.GetResearchArchivesUseCase
import com.researchflow.app.domain.usecase.submission.CreateSignedDocumentUrlUseCase
import com.researchflow.app.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResearchArchiveDetailsUiState(
    val isLoading: Boolean = false,
    val archive: ResearchArchive? = null,
    val student: User? = null,
    val supervisor: User? = null,
    val isOpeningDocument: Boolean = false,
    val documentUrl: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminResearchArchiveDetailsViewModel @Inject constructor(
    private val getResearchArchivesUseCase: GetResearchArchivesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val createSignedDocumentUrlUseCase: CreateSignedDocumentUrlUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ResearchArchiveDetailsUiState()
    )

    val uiState: StateFlow<ResearchArchiveDetailsUiState> =
        _uiState.asStateFlow()

    fun loadArchive(archiveId: String) {
        viewModelScope.launch {

            _uiState.value = ResearchArchiveDetailsUiState(
                isLoading = true
            )

            try {
                val archives = getResearchArchivesUseCase()

                val archive = archives.firstOrNull {
                    it.archiveId == archiveId
                }

                if (archive == null) {
                    _uiState.value = ResearchArchiveDetailsUiState(
                        isLoading = false,
                        errorMessage = "Research archive not found"
                    )
                    return@launch
                }

                val student = getUserUseCase(archive.studentId)
                val supervisor = getUserUseCase(archive.supervisorId)

                _uiState.value = ResearchArchiveDetailsUiState(
                    isLoading = false,
                    archive = archive,
                    student = student,
                    supervisor = supervisor
                )

            } catch (e: Exception) {
                _uiState.value = ResearchArchiveDetailsUiState(
                    isLoading = false,
                    errorMessage = e.message
                        ?: "Failed to load research details"
                )
            }
        }
    }

    fun openDocument() {
        val storagePath = _uiState.value.archive?.storagePath
            ?: return

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isOpeningDocument = true,
                errorMessage = null
            )

            try {
                val signedUrl = createSignedDocumentUrlUseCase(
                    storagePath
                )

                _uiState.value = _uiState.value.copy(
                    isOpeningDocument = false,
                    documentUrl = signedUrl
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isOpeningDocument = false,
                    errorMessage = e.message
                        ?: "Unable to open document"
                )
            }
        }
    }

    fun clearDocumentUrl() {
        _uiState.value = _uiState.value.copy(
            documentUrl = null
        )
    }
}
