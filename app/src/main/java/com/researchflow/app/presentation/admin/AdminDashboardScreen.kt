package com.researchflow.app.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardScreen(
    onSupervisorAssignmentClick: () -> Unit,
    onManageUsersClick: () -> Unit,
    onResearchArchiveClick: () -> Unit,
    onReportsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to ResearchFlow",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSupervisorAssignmentClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Assign Supervisors")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onManageUsersClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Users")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onResearchArchiveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Research Archive")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onReportsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Reports")
        }
    }
}