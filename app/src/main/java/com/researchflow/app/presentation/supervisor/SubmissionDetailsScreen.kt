package com.researchflow.app.presentation.supervisor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.researchflow.app.data.model.SubmissionVersion
import androidx.core.net.toUri
import com.google.firebase.Timestamp
import com.researchflow.app.data.model.SubmissionStatus
import java.util.Calendar

@Composable
fun SubmissionDetailsScreen(
    submissionId: String,
    viewModel: SubmissionDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var feedbackText by remember {
        mutableStateOf("")
    }

    var showDeadlineDialog by remember {
        mutableStateOf(false)
    }

    var deadlineTitle by remember {
        mutableStateOf("")
    }

    var deadlineDescription by remember {
        mutableStateOf("")
    }

    var selectedDeadlineDate by remember {
        mutableStateOf<Calendar?>(null)
    }

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
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Student ID: ${submission.studentId}"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Status: ${submission.status}"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Current Version: ${submission.currentVersion}"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Supervisor ID: ${submission.supervisorId}"
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Deadline",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val deadline = uiState.deadline

                        if (deadline == null) {

                            Text("No deadline set.")

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    deadlineTitle = ""
                                    deadlineDescription = ""
                                    selectedDeadlineDate = null
                                    showDeadlineDialog = true
                                }
                            ) {
                                Text("Set Deadline")
                            }

                        } else {

                            Text(
                                text = deadline.title,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            deadline.deadlineDate?.let {
                                Text(
                                    text = "Due: ${it.toDate()}"
                                )
                            }

                            if (deadline.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = deadline.description
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (deadline.isActive) {

                                Button(
                                    onClick = {
                                        deadlineTitle = deadline.title
                                        deadlineDescription = deadline.description

                                        deadline.deadlineDate?.let {
                                            selectedDeadlineDate =
                                                Calendar.getInstance().apply {
                                                    time = it.toDate()
                                                }
                                        }

                                        showDeadlineDialog = true
                                    }
                                ) {
                                    Text("Edit Deadline")
                                }

                                TextButton(
                                    onClick = {
                                        viewModel.setDeadlineActive(
                                            deadlineId = deadline.deadlineId,
                                            isActive = false
                                        )
                                    }
                                ) {
                                    Text("Deactivate Deadline")
                                }

                            } else {

                                Text("Deadline is inactive.")

                                TextButton(
                                    onClick = {
                                        viewModel.setDeadlineActive(
                                            deadlineId = deadline.deadlineId,
                                            isActive = true
                                        )
                                    }
                                ) {
                                    Text("Activate Deadline")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Documents",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState.versions.isEmpty()) {

                            Text(
                                text = "No uploaded document found."
                            )

                        } else {

                            uiState.versions
                                .sortedByDescending { it.versionNumber }
                                .forEach { version ->

                                    SubmissionVersionItem(
                                        version = version,
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

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                        }

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Text(
                            text = "Supervisor Feedback",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        if (uiState.feedback.isEmpty()) {
                            Text(
                                text = "No feedback has been provided yet.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            uiState.versions
                                .sortedByDescending { it.versionNumber }
                                .forEach { version ->

                                    val versionFeedback = uiState.feedback.filter {
                                        it.versionId == version.versionId
                                    }

                                    if (versionFeedback.isNotEmpty()) {
                                        Text(
                                            text = "Version ${version.versionNumber}",
                                            style = MaterialTheme.typography.titleSmall
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        versionFeedback.forEach { feedback ->

                                            Card(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 8.dp)
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(16.dp)
                                                ) {
                                                    Text(
                                                        text = "Feedback",
                                                        style = MaterialTheme.typography.titleSmall
                                                    )

                                                    Spacer(modifier = Modifier.height(8.dp))

                                                    Text(
                                                        text = feedback.comment,
                                                        style = MaterialTheme.typography.bodyLarge
                                                    )

                                                    Spacer(modifier = Modifier.height(8.dp))

                                                    Text(
                                                        text = "Supervisor: ${feedback.supervisorId}",
                                                        style = MaterialTheme.typography.bodySmall
                                                    )

                                                    feedback.createdAt?.let {
                                                        Spacer(modifier = Modifier.height(4.dp))

                                                        Text(
                                                            text = "Date: $it",
                                                            style = MaterialTheme.typography.bodySmall
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        OutlinedTextField(
                            value = feedbackText,
                            onValueChange = {
                                feedbackText = it
                            },
                            label = {
                                Text("Add Feedback for Version ${submission.currentVersion}")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                            onClick = {
                                val currentVersion = uiState.versions
                                    .find { it.versionNumber == submission.currentVersion }

                                if (currentVersion != null) {
                                    viewModel.addFeedback(
                                        submissionId = submissionId,
                                        versionId = currentVersion.versionId,
                                        comment = feedbackText
                                    )

                                    feedbackText = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = feedbackText.isNotBlank()
                        ) {
                            Text("Submit Feedback")
                        }



                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Text(
                            text = "Review Submission",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        when (submission.status) {

                            SubmissionStatus.SUBMITTED -> {
                                Button(
                                    onClick = {
                                        viewModel.updateStatus(
                                            submissionId = submission.submissionId,
                                            newStatus = SubmissionStatus.UNDER_REVIEW
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Start Review")
                                }
                            }

                            SubmissionStatus.UNDER_REVIEW -> {

                                Button(
                                    onClick = {
                                        viewModel.updateStatus(
                                            submissionId = submission.submissionId,
                                            newStatus = SubmissionStatus.APPROVED
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Approve Submission")
                                }

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        viewModel.updateStatus(
                                            submissionId = submission.submissionId,
                                            newStatus = SubmissionStatus.CORRECTION_REQUIRED
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Request Corrections")
                                }

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        viewModel.updateStatus(
                                            submissionId = submission.submissionId,
                                            newStatus = SubmissionStatus.REJECTED
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Reject Submission")
                                }
                            }

                            SubmissionStatus.CORRECTION_REQUIRED -> {
                                Text(
                                    text = "Waiting for the student to resubmit corrections.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            SubmissionStatus.RESUBMITTED -> {
                                Button(
                                    onClick = {
                                        viewModel.updateStatus(
                                            submissionId = submission.submissionId,
                                            newStatus = SubmissionStatus.UNDER_REVIEW
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Start Review")
                                }
                            }

                            SubmissionStatus.APPROVED -> {
                                Text(
                                    text = "This submission has been approved.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            SubmissionStatus.REJECTED -> {
                                Text(
                                    text = "This submission has been rejected.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    if (showDeadlineDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeadlineDialog = false
            },
            title = {
                Text(
                    text = if (uiState.deadline == null) {
                        "Set Deadline"
                    } else {
                        "Edit Deadline"
                    }
                )
            },
            text = {
                Column {

                    OutlinedTextField(
                        value = deadlineTitle,
                        onValueChange = {
                            deadlineTitle = it
                        },
                        label = {
                            Text("Deadline Title")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = deadlineDescription,
                        onValueChange = {
                            deadlineDescription = it
                        },
                        label = {
                            Text("Description")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {

                            val calendar =
                                selectedDeadlineDate
                                    ?: Calendar.getInstance()

                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->

                                    val updatedCalendar =
                                        (selectedDeadlineDate?.clone() as? Calendar)
                                            ?: Calendar.getInstance()

                                    updatedCalendar.set(
                                        Calendar.YEAR,
                                        year
                                    )

                                    updatedCalendar.set(
                                        Calendar.MONTH,
                                        month
                                    )

                                    updatedCalendar.set(
                                        Calendar.DAY_OF_MONTH,
                                        dayOfMonth
                                    )

                                    selectedDeadlineDate = updatedCalendar
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedDeadlineDate == null) {
                                "Select Date"
                            } else {
                                "Change Date"
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    selectedDeadlineDate?.let { calendar ->

                        Text(
                            text = "Date: ${
                                calendar.get(Calendar.DAY_OF_MONTH)
                            }/${
                                calendar.get(Calendar.MONTH) + 1
                            }/${
                                calendar.get(Calendar.YEAR)
                            }"
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Button(
                        onClick = {

                            val calendar =
                                selectedDeadlineDate
                                    ?: Calendar.getInstance()

                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->

                                    val updatedCalendar =
                                        (selectedDeadlineDate?.clone() as? Calendar)
                                            ?: Calendar.getInstance()

                                    updatedCalendar.set(
                                        Calendar.HOUR_OF_DAY,
                                        hourOfDay
                                    )

                                    updatedCalendar.set(
                                        Calendar.MINUTE,
                                        minute
                                    )

                                    updatedCalendar.set(
                                        Calendar.SECOND,
                                        0
                                    )

                                    updatedCalendar.set(
                                        Calendar.MILLISECOND,
                                        0
                                    )

                                    selectedDeadlineDate = updatedCalendar
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select Time")
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    selectedDeadlineDate?.let { calendar ->

                        Text(
                            text = "Time: ${
                                String.format(
                                    "%02d:%02d",
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE)
                                )
                            }"
                        )
                    }
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        val calendar =
                            selectedDeadlineDate

                        if (
                            deadlineTitle.isNotBlank() &&
                            calendar != null
                        ) {

                            val timestamp =
                                Timestamp(
                                    calendar.time
                                )

                            val existingDeadline =
                                uiState.deadline

                            if (existingDeadline == null) {

                                viewModel.createDeadline(
                                    submissionId = submissionId,
                                    title = deadlineTitle,
                                    description = deadlineDescription,
                                    deadlineDate = timestamp
                                )

                            } else {

                                viewModel.updateDeadline(
                                    deadlineId =
                                        existingDeadline.deadlineId,
                                    title = deadlineTitle,
                                    description =
                                        deadlineDescription,
                                    deadlineDate = timestamp
                                )
                            }

                            showDeadlineDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showDeadlineDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

}

@Composable
private fun SubmissionVersionItem(
    version: SubmissionVersion,
    onOpenDocument: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = version.documentName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Version: ${version.versionNumber}"
            )

            Spacer(modifier = Modifier.height(4.dp))

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
        }
    }
}