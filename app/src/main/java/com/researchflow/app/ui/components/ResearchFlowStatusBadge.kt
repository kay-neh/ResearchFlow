package com.researchflow.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.ui.theme.StatusApproved
import com.researchflow.app.ui.theme.StatusCorrection
import com.researchflow.app.ui.theme.StatusRejected
import com.researchflow.app.ui.theme.StatusResubmitted
import com.researchflow.app.ui.theme.StatusSubmitted
import com.researchflow.app.ui.theme.StatusUnderReview

@Composable
fun ResearchFlowStatusBadge(
    status: SubmissionStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        SubmissionStatus.SUBMITTED -> StatusSubmitted
        SubmissionStatus.UNDER_REVIEW -> StatusUnderReview
        SubmissionStatus.CORRECTION_REQUIRED -> StatusCorrection
        SubmissionStatus.RESUBMITTED -> StatusResubmitted
        SubmissionStatus.APPROVED -> StatusApproved
        SubmissionStatus.REJECTED -> StatusRejected
    }

    val contentColor = when (status) {
        SubmissionStatus.SUBMITTED -> Color.White
        SubmissionStatus.UNDER_REVIEW -> Color.Black
        SubmissionStatus.CORRECTION_REQUIRED -> Color.White
        SubmissionStatus.RESUBMITTED -> Color.White
        SubmissionStatus.APPROVED -> Color.White
        SubmissionStatus.REJECTED -> Color.White
    }

    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
    ) {
        Text(
            text = status.displayName(),
            color = contentColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun SubmissionStatus.displayName(): String {
    return when (this) {
        SubmissionStatus.SUBMITTED -> "Submitted"
        SubmissionStatus.UNDER_REVIEW -> "Under Review"
        SubmissionStatus.CORRECTION_REQUIRED -> "Correction Required"
        SubmissionStatus.RESUBMITTED -> "Resubmitted"
        SubmissionStatus.APPROVED -> "Approved"
        SubmissionStatus.REJECTED -> "Rejected"
    }
}