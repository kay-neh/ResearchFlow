package com.researchflow.app.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.researchflow.app.data.model.User

@Composable
fun AdminUsersScreen(
    onCreateUserClick: () -> Unit,
    viewModel: AdminUsersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Manage Users",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Button(
            onClick = onCreateUserClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create User")
        }

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

            uiState.users.isEmpty() -> {
                Text(
                    text = "No users have been registered yet.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.users) { user ->
                        UserCard(
                            user = user,
                            onEditClick = { name ->
                                viewModel.updateUser(
                                    userId = user.userId,
                                    name = name
                                )
                            },
                            onToggleActiveClick = {
                                viewModel.setUserActive(
                                    userId = user.userId,
                                    isActive = !user.isActive
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    user: User,
    onEditClick: (String) -> Unit,
    onToggleActiveClick: () -> Unit
) {
    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var editedName by remember {
        mutableStateOf(user.name)
    }

    var editedEmail by remember {
        mutableStateOf(user.email)
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Name: ${user.name}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Email: ${user.email}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Role: ${user.role}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (user.isActive) {
                    "Status: Active"
                } else {
                    "Status: Disabled"
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    editedName = user.name
                    editedEmail = user.email
                    showEditDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onToggleActiveClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (user.isActive) {
                        "Disable User"
                    } else {
                        "Enable User"
                    }
                )
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
            },
            title = {
                Text("Edit User")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = {
                            editedName = it
                        },
                        label = {
                            Text("Full Name")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editedEmail,
                        onValueChange = {},
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = false
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                        onEditClick(editedName)
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}