package com.researchflow.app.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.researchflow.app.data.model.Student
import com.researchflow.app.data.model.User

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
                            supervisors = uiState.supervisors,
                            onAssignSupervisor = { studentId, supervisorId ->
                                viewModel.assignSupervisor(
                                    studentId = studentId,
                                    supervisorId = supervisorId
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminStudentCard(
    student: Student,
    supervisors: List<User>,
    onAssignSupervisor: (String, String) -> Unit
) {
    val assignedSupervisor = supervisors.find {
        it.userId == student.supervisorId
    }

    var expanded by remember { mutableStateOf(false) }

    var selectedSupervisor by remember {
        mutableStateOf(assignedSupervisor)
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

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = if (assignedSupervisor != null) {
                    "Current Supervisor: ${assignedSupervisor.name}"
                } else {
                    "Current Supervisor: Not assigned"
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedSupervisor?.name ?: "Select supervisor",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Supervisor")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    supervisors.forEach { supervisor ->
                        DropdownMenuItem(
                            text = {
                                Text(supervisor.name)
                            },
                            onClick = {
                                selectedSupervisor = supervisor
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = {
                    selectedSupervisor?.let { supervisor ->
                        onAssignSupervisor(
                            student.studentId,
                            supervisor.userId
                        )
                    }
                },
                enabled = selectedSupervisor != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Assign Supervisor")
            }
        }
    }
}

