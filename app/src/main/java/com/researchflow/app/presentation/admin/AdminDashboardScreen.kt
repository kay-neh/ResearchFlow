package com.researchflow.app.presentation.admin

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.researchflow.app.R
import com.researchflow.app.ui.components.ResearchFlowCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onSupervisorAssignmentClick: () -> Unit,
    onManageUsersClick: () -> Unit,
    onResearchArchiveClick: () -> Unit,
    onReportsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "ResearchFlow",
                    fontWeight = FontWeight.SemiBold
                )
            },
            actions = {
                IconButton(
                    onClick = onLogoutClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.logout),
                        contentDescription = "Logout"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Dashboard Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Admin Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Manage users, supervisors, research archives, and system reports.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Administration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminDashboardCard(
                icon = R.drawable.group,
                title = "Assign Supervisors",
                description = "Assign supervisors to students and manage research supervision.",
                onClick = onSupervisorAssignmentClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminDashboardCard(
                icon = R.drawable.person,
                title = "Manage Users",
                description = "Create, update, enable, and disable system users.",
                onClick = onManageUsersClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminDashboardCard(
                icon = R.drawable.archive,
                title = "Research Archive",
                description = "View approved research projects stored in the archive.",
                onClick = onResearchArchiveClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminDashboardCard(
                icon = R.drawable.assessment,
                title = "Generate Reports",
                description = "View system statistics and research submission reports.",
                onClick = onReportsClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AdminDashboardCard(
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    ResearchFlowCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}