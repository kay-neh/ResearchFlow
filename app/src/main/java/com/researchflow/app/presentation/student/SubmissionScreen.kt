package com.researchflow.app.presentation.student

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.researchflow.app.data.model.DocumentType

@Composable
fun SubmissionScreen(
    onSubmissionSuccess: () -> Unit,
    viewModel: SubmissionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }
    var selectedDocumentType by remember { mutableStateOf<DocumentType?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedFileUri = uri

            val fileName = uri.lastPathSegment
                ?.substringAfterLast('/')
                ?: "Selected document"

            selectedFileName = fileName

            selectedDocumentType = when {
                fileName.endsWith(".pdf", ignoreCase = true) ->
                    DocumentType.PDF

                fileName.endsWith(".docx", ignoreCase = true) ->
                    DocumentType.DOCX

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
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Submit Research",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Research Title") },
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                filePickerLauncher.launch(
                    arrayOf(
                        "application/pdf",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select PDF or DOCX")
        }

        if (selectedFileName.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Selected file: $selectedFileName",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    val uri = selectedFileUri
                    val documentType = selectedDocumentType

                    if (
                        title.isNotBlank() &&
                        uri != null &&
                        documentType != null
                    ) {
                        viewModel.submitResearch(
                            title = title.trim(),
                            fileUri = uri,
                            fileName = selectedFileName,
                            documentType = documentType
                        )
                    }
                },
                enabled = title.isNotBlank() &&
                        selectedFileUri != null &&
                        selectedDocumentType != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Research")
            }
        }

        uiState.errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}