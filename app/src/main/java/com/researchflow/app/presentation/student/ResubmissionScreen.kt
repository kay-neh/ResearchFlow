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
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.DocumentType

@Composable
fun ResubmissionScreen(
    submissionId: String,
    currentVersion: Int,
    onResubmissionSuccess: () -> Unit,
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Resubmit Research",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Current Version: $currentVersion"
        )

        Text(
            text = "New Version: ${currentVersion + 1}"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                filePicker.launch(
                    arrayOf(
                        "application/pdf",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Corrected Document")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (
            selectedFileUri != null &&
            selectedDocumentType != null
        ) {
            Text(
                text = "Selected: $selectedFileName"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
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
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Submit Correction")
            }
        }

        if (uiState.isLoading) {
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            CircularProgressIndicator()
        }

        uiState.errorMessage?.let { error ->
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}