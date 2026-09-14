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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.ResearchArchive
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun AdminResearchArchiveScreen(
    viewModel: AdminResearchArchiveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadArchives()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Research Archive")
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
                            ?: "Failed to load research archives",
                        color = MaterialTheme.colorScheme.error
                    )

                    TextButton(
                        onClick = { viewModel.refresh() }
                    ) {
                        Text("Retry")
                    }
                }
            }

            uiState.archives.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No archived research yet.",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Approved research submissions will appear here.",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.archives,
                        key = { it.archiveId }
                    ) { archive ->
                        ResearchArchiveCard(archive)
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun ResearchArchiveCard(
    archive: ResearchArchive
) {
    val dateFormat = SimpleDateFormat(
        "dd MMM yyyy, hh:mm a",
        Locale.getDefault()
    )

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = archive.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Document: ${archive.documentName}"
            )

            Text(
                text = "Approved Version: ${archive.approvedVersion}"
            )

            Text(
                text = "Student ID: ${archive.studentId}"
            )

            Text(
                text = "Supervisor ID: ${archive.supervisorId}"
            )

            archive.archivedAt?.let { timestamp ->
                Text(
                    text = "Archived: ${dateFormat.format(timestamp.toDate())}"
                )
            }
        }
    }
}