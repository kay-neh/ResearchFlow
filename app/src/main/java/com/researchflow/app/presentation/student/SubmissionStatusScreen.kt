package com.researchflow.app.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.researchflow.app.core.utils.toDisplayDateTime
import com.researchflow.app.data.model.Submission
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowPrimaryButton
import com.researchflow.app.ui.components.ResearchFlowStatusBadge
import com.researchflow.app.ui.components.ResearchFlowTopAppBar

@Composable
fun SubmissionStatusScreen(
    onSubmissionClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SubmissionStatusViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSubmissions()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ResearchFlowTopAppBar(
            title = "Submission Status",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                )
        ) {

            Text(
                text = "Track the progress of your research submissions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            bottom = 24.dp
                        )
                    ) {
                        items(uiState.submissions) { submission ->
                            SubmissionStatusCard(
                                submission = submission,
                                onSubmissionClick = onSubmissionClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubmissionStatusCard(
    submission: Submission,
    onSubmissionClick: (String) -> Unit
) {
    ResearchFlowCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onSubmissionClick(submission.submissionId)
        }
    ) {

        Text(
            text = submission.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        ResearchFlowStatusBadge(
            status = submission.status
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Version ${submission.currentVersion}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        submission.createdAt?.let {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Submitted: ${it.toDisplayDateTime()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        submission.updatedAt?.let {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Last updated: ${it.toDisplayDateTime()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "View submission details",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}