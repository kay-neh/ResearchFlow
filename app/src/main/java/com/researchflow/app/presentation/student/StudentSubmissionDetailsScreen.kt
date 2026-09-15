package com.researchflow.app.presentation.student

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.Feedback
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowPrimaryButton
import com.researchflow.app.ui.components.ResearchFlowSecondaryButton
import com.researchflow.app.ui.components.ResearchFlowStatusBadge
import com.researchflow.app.ui.components.ResearchFlowTopAppBar
import androidx.compose.ui.platform.LocalContext
import com.researchflow.app.core.utils.toDisplayDateTime

@Composable
fun StudentSubmissionDetailsScreen(
    submissionId: String,
    onResubmitClick: (String, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: StudentSubmissionDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(submissionId) {
        viewModel.loadSubmission(submissionId)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ResearchFlowTopAppBar(
            title = "Submission Details",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage
                            ?: "Failed to load submission",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                uiState.submission != null -> {
                    val submission = uiState.submission!!

                    SubmissionOverviewCard(
                        title = submission.title,
                        status = submission.status,
                        currentVersion = submission.currentVersion,
                        createdAt = submission.createdAt?.toDisplayDateTime(),
                        updatedAt = submission.updatedAt?.toDisplayDateTime()
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    SectionHeader(
                        title = "Submission Versions",
                        subtitle = "Review your uploaded research documents and supervisor feedback."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (uiState.versions.isEmpty()) {
                        ResearchFlowCard {
                            Text(
                                text = "No uploaded versions found.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        uiState.versions
                            .sortedByDescending { it.versionNumber }
                            .forEach { version ->

                                StudentSubmissionVersionItem(
                                    version = version,
                                    feedback = uiState.feedback.filter {
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

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                    }

                    if (
                        submission.status ==
                        SubmissionStatus.CORRECTION_REQUIRED
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        ResearchFlowCard {
                            Column {
                                Text(
                                    text = "Corrections Required",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Your supervisor has requested corrections to this submission. Upload a revised version to continue the review process.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                ResearchFlowPrimaryButton(
                                    text = "Resubmit Correction",
                                    onClick = {
                                        onResubmitClick(
                                            submission.submissionId,
                                            submission.currentVersion
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SubmissionOverviewCard(
    title: String,
    status: SubmissionStatus,
    currentVersion: Int,
    createdAt: String?,
    updatedAt: String?
) {
    ResearchFlowCard {
        Column {
            Text(
                text = "Research Submission",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResearchFlowStatusBadge(
                status = status
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                label = "Current version",
                value = "Version $currentVersion"
            )

            createdAt?.let {
                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(
                    label = "Submitted",
                    value = it
                )
            }

            updatedAt?.let {
                Spacer(modifier = Modifier.height(10.dp))

                InfoRow(
                    label = "Last updated",
                    value = it
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StudentSubmissionVersionItem(
    version: SubmissionVersion,
    feedback: List<Feedback>,
    onOpenDocument: () -> Unit
) {
    ResearchFlowCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Version ${version.versionNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = version.documentName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = version.documentType.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ResearchFlowSecondaryButton(
                text = "Open Document",
                onClick = onOpenDocument,
                enabled = version.storagePath.isNotBlank()
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Supervisor Feedback",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (feedback.isEmpty()) {
                Text(
                    text = "No feedback for this version yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    feedback.forEach { item ->
                        ResearchFlowCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.comment,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            item.createdAt?.let {
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = it.toDisplayDateTime(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Loading submission...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

