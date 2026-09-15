package com.researchflow.app.presentation.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.researchflow.app.R
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowPrimaryButton
import com.researchflow.app.ui.components.ResearchFlowSecondaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onSubmitResearchClick: () -> Unit,
    onViewSubmissionStatusClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Student Dashboard")
                },
                actions = {
                    IconButton(
                        onClick = onLogoutClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.exit_to_app),
                            contentDescription = "Logout",

                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome to ResearchFlow",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Student Dashboard",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Manage your research submissions, track progress, and stay updated.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            ResearchFlowCard {

                Text(
                    text = "Research Submission",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Submit your research project or check the current status of an existing submission.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                ResearchFlowPrimaryButton(
                    text = "Submit Research",
                    onClick = onSubmitResearchClick
                )

                Spacer(modifier = Modifier.height(10.dp))

                ResearchFlowSecondaryButton(
                    text = "View Submission Status",
                    onClick = onViewSubmissionStatusClick
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ResearchFlowCard {

                Text(
                    text = "Stay Updated",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Check your latest notifications and supervisor updates.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                ResearchFlowPrimaryButton(
                    text = "Notifications",
                    onClick = onNotificationsClick
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ResearchFlowCard {

                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "View your personal and academic information.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                ResearchFlowSecondaryButton(
                    text = "View My Profile",
                    onClick = onProfileClick
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}