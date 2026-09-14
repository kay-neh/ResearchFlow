package com.researchflow.app.presentation.student

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.SubmissionVersion

@Composable
fun StudentSubmissionDetailsScreen(
    submissionId: String,
    onResubmitClick: (String, Int) -> Unit,
    viewModel: StudentSubmissionDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(submissionId) {
        viewModel.loadSubmission(submissionId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Submission Details",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage
                        ?: "Failed to load submission",
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.submission != null -> {
                val submission = uiState.submission!!

                Text(
                    text = submission.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Status: ${submission.status}")
                Text("Current Version: ${submission.currentVersion}")

                submission.createdAt?.let {
                    Text("Submitted: $it")
                }

                submission.updatedAt?.let {
                    Text("Last updated: $it")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Submission Versions",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.versions.isEmpty()) {
                    Text("No uploaded versions found.")
                } else {
                    uiState.versions
                        .sortedByDescending { it.versionNumber }
                        .forEach { version ->

                            StudentSubmissionVersionItem(
                                version = version,
                                feedback = uiState.feedback
                                    .filter {
                                        it.versionId == version.versionId
                                    },
                                onOpenDocument = {
                                    viewModel.openDocument(
                                        storagePath = version.storagePath
                                    ) { signedUrl ->

                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(signedUrl)
                                        )

                                        context.startActivity(intent)
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                }

                if (
                    submission.status ==
                    com.researchflow.app.data.model.SubmissionStatus.CORRECTION_REQUIRED
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onResubmitClick(
                                submission.submissionId,
                                submission.currentVersion
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Resubmit Correction")
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentSubmissionVersionItem(
    version: SubmissionVersion,
    feedback: List<com.researchflow.app.data.model.Feedback>,
    onOpenDocument: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Version ${version.versionNumber}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = version.documentName
            )

            Text(
                text = "Type: ${version.documentType}"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onOpenDocument,
                modifier = Modifier.fillMaxWidth(),
                enabled = version.storagePath.isNotBlank()
            ) {
                Text("Open Document")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Feedback",
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (feedback.isEmpty()) {
                Text(
                    text = "No feedback for this version.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                feedback.forEach { item ->
                    Text(
                        text = item.comment,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}