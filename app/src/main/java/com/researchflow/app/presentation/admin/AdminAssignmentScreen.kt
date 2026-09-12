package com.researchflow.app.presentation.admin

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
fun AdminAssignmentScreen(
    viewModel: AdminAssignmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAssignmentData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Supervisor Assignment",
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

            uiState.students.isEmpty() -> {
                Text(
                    text = "No students have been registered yet.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.students) { student ->
                        AdminStudentCard(
                            student = student,
                            supervisors = uiState.supervisors
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStudentCard(
    student: com.researchflow.app.data.model.Student,
    supervisors: List<com.researchflow.app.data.model.User>
) {
    val assignedSupervisor = supervisors.find {
        it.userId == student.supervisorId
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Matric Number: ${student.matricNumber}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "Department: ${student.department}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = if (assignedSupervisor != null) {
                    "Supervisor: ${assignedSupervisor.name}"
                } else {
                    "Supervisor: Not assigned"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}