package com.researchflow.app.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SubmissionStatusScreen(
    viewModel: SubmissionStatusViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSubmissions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Submission Status",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.padding(12.dp))

        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.submissions.isEmpty() -> {
                Text(
                    text = "You have no research submissions yet.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.submissions) { submission ->
                        SubmissionStatusCard(
                            submission = submission
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubmissionStatusCard(
    submission: com.researchflow.app.data.model.Submission
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = submission.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.padding(6.dp))

            Text(
                text = "Status: ${submission.status}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Version: ${submission.currentVersion}",
                style = MaterialTheme.typography.bodyMedium
            )

            submission.createdAt?.let {
                Text(
                    text = "Submitted: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            submission.updatedAt?.let {
                Text(
                    text = "Last updated: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}