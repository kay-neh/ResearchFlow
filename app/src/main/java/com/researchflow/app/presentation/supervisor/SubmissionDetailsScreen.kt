package com.researchflow.app.presentation.supervisor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.Timestamp
import com.researchflow.app.R
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.data.model.SubmissionVersion
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowStatusBadge
import com.researchflow.app.ui.components.ResearchFlowTopAppBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SubmissionDetailsScreen(
    submissionId: String,
    onBackClick: () -> Unit,
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
        modifier = Modifier.fillMaxSize()
    ) {

        ResearchFlowTopAppBar(
            title = "Submission Details",
            onBackClick = onBackClick
        )

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.errorMessage
                            ?: "Failed to load submission",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            uiState.submission != null -> {
                val submission = uiState.submission!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Review and manage this research submission.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Submission overview
                    ResearchFlowCard {

                        Row(
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.description),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )

                            Spacer(modifier = Modifier.size(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = submission.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                ResearchFlowStatusBadge(
                                    status = submission.status
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        SubmissionInfoRow(
                            label = "Student ID",
                            value = submission.studentId,
                            painter = painterResource(R.drawable.description)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SubmissionInfoRow(
                            label = "Current Version",
                            value = "Version ${submission.currentVersion}",
                            painter = painterResource(R.drawable.description)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SubmissionInfoRow(
                            label = "Supervisor ID",
                            value = submission.supervisorId,
                            painter = painterResource(R.drawable.person)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Deadline
                    SectionHeader(
                        title = "Deadline",
                        painter = painterResource(R.drawable.calendar_month)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val deadline = uiState.deadline

                    ResearchFlowCard {

                        if (deadline == null) {

                            Text(
                                text = "No deadline has been set for this submission.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    deadlineTitle = ""
                                    deadlineDescription = ""
                                    selectedDeadlineDate = null
                                    showDeadlineDialog = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.calendar_month),
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.size(8.dp))

                                Text("Set Deadline")
                            }

                        } else {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.schedule),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.size(10.dp))

                                Column {
                                    Text(
                                        text = deadline.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    deadline.deadlineDate?.let {
                                        Text(
                                            text = formatDateTime(it),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            if (deadline.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = deadline.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

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
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Edit Deadline")
                                }

                                TextButton(
                                    onClick = {
                                        viewModel.setDeadlineActive(
                                            deadlineId = deadline.deadlineId,
                                            isActive = false
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Deactivate Deadline")
                                }

                            } else {

                                Text(
                                    text = "This deadline is currently inactive.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
                                    onClick = {
                                        viewModel.setDeadlineActive(
                                            deadlineId = deadline.deadlineId,
                                            isActive = true
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Activate Deadline")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Documents
                    SectionHeader(
                        title = "Documents",
                        painter = painterResource(R.drawable.description)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.versions.isEmpty()) {

                        ResearchFlowCard {
                            Text(
                                text = "No uploaded document found.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Feedback
                    SectionHeader(
                        title = "Supervisor Feedback",
                        painter = painterResource(R.drawable.feedback)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.feedback.isEmpty()) {

                        ResearchFlowCard {
                            Text(
                                text = "No feedback has been provided yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

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
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    versionFeedback.forEach { feedback ->

                                        ResearchFlowCard {

                                            Text(
                                                text = feedback.comment,
                                                style = MaterialTheme.typography.bodyLarge
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Supervisor: ${feedback.supervisorId}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )

                                            feedback.createdAt?.let {
                                                Spacer(
                                                    modifier = Modifier.height(4.dp)
                                                )

                                                Text(
                                                    text = formatDateTime(it),
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = {
                            feedbackText = it
                        },
                        label = {
                            Text(
                                "Add feedback for Version ${submission.currentVersion}"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val currentVersion = uiState.versions
                                .find {
                                    it.versionNumber == submission.currentVersion
                                }

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
                        Icon(
                            painter = painterResource(R.drawable.feedback),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text("Submit Feedback")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Review actions
                    SectionHeader(
                        title = "Review Submission",
                        painter = painterResource(R.drawable.rate_review)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ResearchFlowCard {

                        when (submission.status) {

                            SubmissionStatus.SUBMITTED -> {

                                Text(
                                    text = "This submission is waiting to be reviewed.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

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

                                Text(
                                    text = "Review this submission and choose an appropriate outcome.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(14.dp))

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

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
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

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedButton(
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
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            SubmissionStatus.RESUBMITTED -> {

                                Text(
                                    text = "The student has submitted a corrected version for review.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

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
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            SubmissionStatus.REJECTED -> {

                                Text(
                                    text = "This submission has been rejected.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Deadline dialog
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

                    Spacer(modifier = Modifier.height(12.dp))

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

                    Spacer(modifier = Modifier.height(12.dp))

                    val calendar =
                        selectedDeadlineDate ?: Calendar.getInstance()

                    Button(
                        onClick = {

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
                            if (selectedDeadlineDate == null) {
                                "Select Date"
                            } else {
                                "Change Date"
                            }
                        )
                    }

                    selectedDeadlineDate?.let {

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Date: ${
                                it.get(Calendar.DAY_OF_MONTH)
                            }/${
                                it.get(Calendar.MONTH) + 1
                            }/${
                                it.get(Calendar.YEAR)
                            }",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {

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

                    selectedDeadlineDate?.let {

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Time: ${
                                String.format(
                                    "%02d:%02d",
                                    it.get(Calendar.HOUR_OF_DAY),
                                    it.get(Calendar.MINUTE)
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        val calendar = selectedDeadlineDate

                        if (
                            deadlineTitle.isNotBlank() &&
                            calendar != null
                        ) {

                            val timestamp = Timestamp(calendar.time)

                            val existingDeadline = uiState.deadline

                            if (existingDeadline == null) {

                                viewModel.createDeadline(
                                    submissionId = submissionId,
                                    title = deadlineTitle,
                                    description = deadlineDescription,
                                    deadlineDate = timestamp
                                )

                            } else {

                                viewModel.updateDeadline(
                                    deadlineId = existingDeadline.deadlineId,
                                    title = deadlineTitle,
                                    description = deadlineDescription,
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
private fun SubmissionInfoRow(
    label: String,
    value: String,
    painter: Painter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    painter: Painter
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SubmissionVersionItem(
    version: SubmissionVersion,
    onOpenDocument: () -> Unit
) {
    ResearchFlowCard {

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.description),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = version.documentName,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Version ${version.versionNumber}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = version.documentType.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

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

private fun formatDateTime(timestamp: Timestamp): String {
    val formatter = SimpleDateFormat(
        "dd MMM yyyy, h:mm a",
        Locale.getDefault()
    )

    return formatter.format(timestamp.toDate())
}