package com.researchflow.app.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.AdminReport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportsScreen(
    viewModel: AdminReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReport()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Generate Reports")
                }
            )
        }
    ) { paddingValues ->

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.errorMessage
                            ?: "Failed to generate report",
                        color = MaterialTheme.colorScheme.error
                    )

                    TextButton(
                        onClick = { viewModel.refresh() }
                    ) {
                        Text("Retry")
                    }
                }
            }

            uiState.report != null -> {
                ReportContent(
                    report = uiState.report!!,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun ReportContent(
    report: AdminReport,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            ReportCard(
                title = "Total Users",
                value = report.totalUsers
            )
        }

        item {
            ReportCard(
                title = "Total Students",
                value = report.totalStudents
            )
        }

        item {
            ReportCard(
                title = "Total Supervisors",
                value = report.totalSupervisors
            )
        }

        item {
            ReportCard(
                title = "Total Submissions",
                value = report.totalSubmissions
            )
        }

        item {
            Text(
                text = "Submission Status",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 4.dp
                )
            )
        }

        item {
            ReportCard(
                title = "Submitted",
                value = report.submitted
            )
        }

        item {
            ReportCard(
                title = "Under Review",
                value = report.underReview
            )
        }

        item {
            ReportCard(
                title = "Correction Required",
                value = report.correctionRequired
            )
        }

        item {
            ReportCard(
                title = "Resubmitted",
                value = report.resubmitted
            )
        }

        item {
            ReportCard(
                title = "Approved",
                value = report.approved
            )
        }

        item {
            ReportCard(
                title = "Rejected",
                value = report.rejected
            )
        }
    }
}

@Composable
private fun ReportCard(
    title: String,
    value: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}