package com.researchflow.app.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.researchflow.app.data.model.AdminReport
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowTopAppBar

@Composable
fun AdminReportsScreen(
    onBackClick: () -> Unit,
    viewModel: AdminReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadReport()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ResearchFlowTopAppBar(
            title = "Generate Reports",
            onBackClick = onBackClick
        )

        when {
            uiState.isLoading -> {
                LoadingReportState()
            }

            uiState.errorMessage != null -> {
                ErrorReportState(
                    message = uiState.errorMessage
                        ?: "Failed to generate report",
                    onRetry = {
                        viewModel.refresh()
                    }
                )
            }

            uiState.report != null -> {
                ReportContent(
                    report = uiState.report!!
                )
            }
        }
    }
}

@Composable
private fun LoadingReportState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Generating report...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorReportState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ResearchFlowCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onRetry
                ) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
private fun ReportContent(
    report: AdminReport
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            Text(
                text = "System Overview",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Current statistics for users, supervisors, students, and research submissions.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // First row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReportMetricCard(
                    title = "Total Users",
                    value = report.totalUsers,
                    modifier = Modifier.weight(1f)
                )

                ReportMetricCard(
                    title = "Students",
                    value = report.totalStudents,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Second row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReportMetricCard(
                    title = "Supervisors",
                    value = report.totalSupervisors,
                    modifier = Modifier.weight(1f)
                )

                ReportMetricCard(
                    title = "Submissions",
                    value = report.totalSubmissions,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Submission Status",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Overview of research submissions by their current status.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            ReportStatusCard(
                title = "Submitted",
                value = report.submitted
            )
        }

        item {
            ReportStatusCard(
                title = "Under Review",
                value = report.underReview
            )
        }

        item {
            ReportStatusCard(
                title = "Correction Required",
                value = report.correctionRequired
            )
        }

        item {
            ReportStatusCard(
                title = "Resubmitted",
                value = report.resubmitted
            )
        }

        item {
            ReportStatusCard(
                title = "Approved",
                value = report.approved
            )
        }

        item {
            ReportStatusCard(
                title = "Rejected",
                value = report.rejected
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ReportMetricCard(
    title: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    ResearchFlowCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ReportStatusCard(
    title: String,
    value: Int
) {
    ResearchFlowCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
