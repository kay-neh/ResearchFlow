package com.researchflow.app.presentation.student

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.DocumentType
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowPrimaryButton
import com.researchflow.app.ui.components.ResearchFlowSecondaryButton
import com.researchflow.app.ui.components.ResearchFlowTopAppBar

@Composable
fun ResubmissionScreen(
    submissionId: String,
    currentVersion: Int,
    onResubmissionSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ResubmissionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    var selectedFileUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedFileName by remember {
        mutableStateOf("")
    }

    var selectedDocumentType by remember {
        mutableStateOf<DocumentType?>(null)
    }

    val filePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri != null) {
                selectedFileUri = uri

                val mimeType =
                    context.contentResolver.getType(uri)

                selectedDocumentType =
                    when (mimeType) {
                        "application/pdf" ->
                            DocumentType.PDF

                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                            DocumentType.DOCX

                        else ->
                            null
                    }

                selectedFileName =
                    context.contentResolver.query(
                        uri,
                        arrayOf(OpenableColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                    OpenableColumns.DISPLAY_NAME
                                )
                            )
                        } else {
                            null
                        }
                    } ?: "corrected_document"
            }
        }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onResubmissionSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ResearchFlowTopAppBar(
            title = "Resubmit Research",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Submit a corrected version of your research project.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            ResearchFlowCard {
                Column {
                    Text(
                        text = "Version Information",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    VersionInfoRow(
                        label = "Current version",
                        value = "Version $currentVersion"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    VersionInfoRow(
                        label = "New version",
                        value = "Version ${currentVersion + 1}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ResearchFlowCard {
                Column {
                    Text(
                        text = "Corrected Document",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Upload the corrected PDF or DOCX version of your research.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ResearchFlowSecondaryButton(
                        text = "Select Corrected Document",
                        onClick = {
                            filePicker.launch(
                                arrayOf(
                                    "application/pdf",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                )
                            )
                        },
                        enabled = !uiState.isLoading
                    )

                    if (
                        selectedFileUri != null &&
                        selectedDocumentType != null
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        SelectedFileCard(
                            fileName = selectedFileName,
                            documentType = selectedDocumentType!!
                        )
                    }
                }
            }

            if (
                selectedFileUri != null &&
                selectedDocumentType != null
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                ResearchFlowPrimaryButton(
                    text = "Submit Correction",
                    onClick = {
                        viewModel.resubmit(
                            submissionId = submissionId,
                            currentVersion = currentVersion,
                            fileUri = selectedFileUri!!,
                            fileName = selectedFileName,
                            documentType = selectedDocumentType
                                ?: DocumentType.PDF
                        )
                    },
                    enabled = !uiState.isLoading
                )
            }

            if (uiState.isLoading) {
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Uploading your correction...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(20.dp))

                ResearchFlowCard {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun VersionInfoRow(
    label: String,
    value: String
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SelectedFileCard(
    fileName: String,
    documentType: DocumentType
) {
    ResearchFlowCard {
        Column {
            Text(
                text = "Selected document",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = fileName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Format: $documentType",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
