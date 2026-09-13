package com.researchflow.app.core.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.researchflow.app.data.model.UserRole
import com.researchflow.app.presentation.admin.AdminAssignmentScreen
import com.researchflow.app.presentation.admin.AdminCreateUserScreen
import com.researchflow.app.presentation.admin.AdminDashboardScreen
import com.researchflow.app.presentation.admin.AdminUsersScreen
import com.researchflow.app.presentation.auth.LoginScreen
import com.researchflow.app.presentation.auth.RegisterScreen
import com.researchflow.app.presentation.student.StudentDashboardScreen
import com.researchflow.app.presentation.student.StudentProfileScreen
import com.researchflow.app.presentation.student.SubmissionScreen
import com.researchflow.app.presentation.student.SubmissionStatusScreen
import com.researchflow.app.presentation.supervisor.SupervisorDashboardScreen
import com.researchflow.app.presentation.supervisor.SupervisorStudentsScreen

@Composable
fun AppNavigation() {

    val navigationState = rememberNavigationState(
        startRoute = Login,
        topLevelRoutes = setOf(Login)
    )

    val navigator = remember {
        Navigator(navigationState)
    }

    val entryProvider = entryProvider<NavKey> {

        entry<Login> {
            LoginScreen(
                onRegisterClick = {
                    navigator.navigate(Register)
                },
                onLoginSuccess = { role ->
                    when (role) {
                        UserRole.STUDENT -> {
                            navigator.navigate(StudentDashboard)
                        }

                        UserRole.SUPERVISOR -> {
                            navigator.navigate(SupervisorDashboard)
                        }

                        UserRole.ADMIN -> {
                            navigator.navigate(AdminDashboard)
                        }
                    }
                }
            )
        }

        entry<Register> {
            RegisterScreen(
                onRegistrationSuccess = {
                    navigator.navigate(StudentProfile)
                }
            )
        }

        entry<StudentProfile> {
            StudentProfileScreen(
                onProfileSaved = {
                    navigator.navigate(StudentDashboard)
                }
            )
        }

        entry<StudentDashboard> {
            StudentDashboardScreen(
                onSubmitResearchClick = {
                    navigator.navigate(SubmitResearch)
                },
                onViewSubmissionStatusClick = {
                    navigator.navigate(SubmissionStatus)
                }
            )
        }

        entry<SubmitResearch> {
            SubmissionScreen(
                onSubmissionSuccess = {
                    navigator.navigate(StudentDashboard)
                }
            )
        }

        entry<SubmissionStatus> {
            SubmissionStatusScreen()
        }

        entry<SupervisorDashboard> {
            SupervisorDashboardScreen(
                onViewStudentsClick = {
                    navigator.navigate(SupervisorStudents)
                }
            )
        }

        entry<SupervisorStudents> {
            SupervisorStudentsScreen()
        }

        entry<AdminDashboard> {
            AdminDashboardScreen(
                onSupervisorAssignmentClick = {
                    navigator.navigate(
                        SupervisorAssignment
                    )
                },
                onManageUsersClick = {
                    navigator.navigate(
                        ManageUsers
                    )
                }
            )
        }

        entry<SupervisorAssignment> {
            AdminAssignmentScreen()
        }

        entry<ManageUsers> {
            AdminUsersScreen(
                onCreateUserClick = {
                    navigator.navigate(CreateUser)
                }
            )
        }

        entry<CreateUser> {
            AdminCreateUserScreen(
                onUserCreated = {
                    navigator.goBack()
                }
            )
        }

    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = {
            navigator.goBack()
        }
    )
}