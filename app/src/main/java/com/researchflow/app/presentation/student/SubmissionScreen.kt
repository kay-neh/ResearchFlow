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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.researchflow.app.data.model.DocumentType
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowPrimaryButton
import com.researchflow.app.ui.components.ResearchFlowSecondaryButton
import com.researchflow.app.ui.components.ResearchFlowTopAppBar

@Composable
fun SubmissionScreen(
    onSubmissionSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SubmissionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var title by remember {
        mutableStateOf("")
    }

    var selectedFileUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedFileName by remember {
        mutableStateOf("")
    }

    var selectedDocumentType by remember {
        mutableStateOf<DocumentType?>(null)
    }

    val filePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {
                selectedFileUri = uri

                val fileName =
                    context.contentResolver
                        .query(
                            uri,
                            arrayOf(OpenableColumns.DISPLAY_NAME),
                            null,
                            null,
                            null
                        )
                        ?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                        OpenableColumns.DISPLAY_NAME
                                    )
                                )
                            } else {
                                null
                            }
                        }
                        ?: "Selected document"

                selectedFileName = fileName

                val mimeType =
                    context.contentResolver.getType(uri)

                selectedDocumentType =
                    when {
                        mimeType == "application/pdf" -> {
                            DocumentType.PDF
                        }

                        mimeType ==
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> {
                            DocumentType.DOCX
                        }

                        fileName.endsWith(
                            ".pdf",
                            ignoreCase = true
                        ) -> {
                            DocumentType.PDF
                        }

                        fileName.endsWith(
                            ".docx",
                            ignoreCase = true
                        ) -> {
                            DocumentType.DOCX
                        }

                        else -> null
                    }
            }
        }

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) {
            onSubmissionSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ResearchFlowTopAppBar(
            title = "Submit Research",
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
                text = "Submit your research project for supervisor review.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            ResearchFlowCard {
                Column {
                    Text(
                        text = "Research Information",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Enter the title of your research project.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        label = {
                            Text("Research Title")
                        },
                        placeholder = {
                            Text("Enter your research title")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ResearchFlowCard {
                Column {
                    Text(
                        text = "Research Document",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Upload your research document in PDF or DOCX format.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ResearchFlowSecondaryButton(
                        text = "Select PDF or DOCX",
                        onClick = {
                            filePickerLauncher.launch(
                                arrayOf(
                                    "application/pdf",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                )
                            )
                        },
                        enabled = !uiState.isLoading
                    )

                    if (
                        selectedFileName.isNotEmpty() &&
                        selectedDocumentType != null
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        SelectedDocumentCard(
                            fileName = selectedFileName,
                            documentType = selectedDocumentType!!
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Submitting your research...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val canSubmit =
                    title.isNotBlank() &&
                            selectedFileUri != null &&
                            selectedDocumentType != null

                ResearchFlowPrimaryButton(
                    text = "Submit Research",
                    onClick = {
                        val uri = selectedFileUri
                        val documentType = selectedDocumentType

                        if (
                            uri != null &&
                            documentType != null &&
                            title.isNotBlank()
                        ) {
                            viewModel.submitResearch(
                                title = title.trim(),
                                fileUri = uri,
                                fileName = selectedFileName,
                                documentType = documentType
                            )
                        }
                    },
                    enabled = canSubmit
                )
            }

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(20.dp))

                ResearchFlowCard {
                    Text(
                        text = message,
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
private fun SelectedDocumentCard(
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