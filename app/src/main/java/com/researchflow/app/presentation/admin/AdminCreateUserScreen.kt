package com.researchflow.app.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.researchflow.app.data.model.UserRole
import com.researchflow.app.ui.components.ResearchFlowCard
import com.researchflow.app.ui.components.ResearchFlowTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateUserScreen(
    viewModel: AdminCreateUserViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUserCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var matricNumber by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    var selectedRole by remember {
        mutableStateOf(UserRole.STUDENT)
    }

    var roleMenuExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onUserCreated()
            viewModel.clearState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ResearchFlowTopAppBar(
            title = "Create User",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Create a user account",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Create an account for a student or supervisor.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            ResearchFlowCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Account Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ExposedDropdownMenuBox(
                        expanded = roleMenuExpanded,
                        onExpandedChange = {
                            roleMenuExpanded = !roleMenuExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = when (selectedRole) {
                                UserRole.STUDENT -> "Student"
                                UserRole.SUPERVISOR -> "Supervisor"
                                UserRole.ADMIN -> "Admin"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Account Role") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(
                                    ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true
                                )
                        )

                        ExposedDropdownMenu(
                            expanded = roleMenuExpanded,
                            onDismissRequest = {
                                roleMenuExpanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Student") },
                                onClick = {
                                    selectedRole = UserRole.STUDENT
                                    roleMenuExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Supervisor") },
                                onClick = {
                                    selectedRole = UserRole.SUPERVISOR
                                    roleMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (selectedRole == UserRole.STUDENT) {

                Spacer(modifier = Modifier.height(12.dp))

                ResearchFlowCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "Student Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = matricNumber,
                            onValueChange = { matricNumber = it },
                            label = { Text("Matric Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = department,
                            onValueChange = { department = it },
                            label = { Text("Department") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            if (uiState.errorMessage != null) {

                Spacer(modifier = Modifier.height(12.dp))

                ResearchFlowCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = uiState.errorMessage!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.createUser(
                        name = name,
                        email = email,
                        password = password,
                        role = selectedRole,
                        matricNumber = matricNumber,
                        department = department
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading &&
                        name.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        (
                                selectedRole == UserRole.SUPERVISOR ||
                                        (
                                                matricNumber.isNotBlank() &&
                                                        department.isNotBlank()
                                                )
                                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Create User")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
