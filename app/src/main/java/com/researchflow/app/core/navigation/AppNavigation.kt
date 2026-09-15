package com.researchflow.app.core.navigation

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.researchflow.app.data.model.ResearchArchive
import com.researchflow.app.data.model.UserRole
import com.researchflow.app.presentation.admin.AdminAssignmentScreen
import com.researchflow.app.presentation.admin.AdminCreateUserScreen
import com.researchflow.app.presentation.admin.AdminDashboardScreen
import com.researchflow.app.presentation.admin.AdminReportsScreen
import com.researchflow.app.presentation.admin.AdminResearchArchiveDetailsScreen
import com.researchflow.app.presentation.admin.AdminResearchArchiveScreen
import com.researchflow.app.presentation.admin.AdminUsersScreen
import com.researchflow.app.presentation.auth.AuthViewModel
import com.researchflow.app.presentation.auth.ForgotPasswordScreen
import com.researchflow.app.presentation.auth.LoginScreen
import com.researchflow.app.presentation.auth.RegisterScreen
import com.researchflow.app.presentation.notification.NotificationScreen
import com.researchflow.app.presentation.student.ResubmissionScreen
import com.researchflow.app.presentation.student.StudentAccountProfileScreen
import com.researchflow.app.presentation.student.StudentDashboardScreen
import com.researchflow.app.presentation.student.StudentProfileScreen
import com.researchflow.app.presentation.student.StudentSubmissionDetailsScreen
import com.researchflow.app.presentation.student.SubmissionScreen
import com.researchflow.app.presentation.student.SubmissionStatusScreen
import com.researchflow.app.presentation.supervisor.SubmissionDetailsScreen
import com.researchflow.app.presentation.supervisor.SupervisorAccountProfileScreen
import com.researchflow.app.presentation.supervisor.SupervisorDashboardScreen
import com.researchflow.app.presentation.supervisor.SupervisorStudentsScreen
import com.researchflow.app.presentation.supervisor.SupervisorSubmissionsScreen

@Composable
fun AppNavigation() {

    val context = LocalContext.current
    val activity = context as? Activity

    val authViewModel: AuthViewModel = hiltViewModel()

    val navigationState = rememberNavigationState(
        startRoute = Login,
        topLevelRoutes = setOf(
            Login,
            StudentDashboard,
            SupervisorDashboard,
            AdminDashboard
        )
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
                },
                onForgotPasswordClick = {
                    navigator.navigate(ForgotPassword)
                },
                viewModel = authViewModel
            )
        }

        entry<Register> {
            RegisterScreen(
                onRegistrationSuccess = {
                    navigator.navigate(StudentProfile)
                },
                onLoginClick = {
                    navigator.goBack()
                }
            )
        }

        entry<ForgotPassword> {
            ForgotPasswordScreen(
                onBackToLoginClick = {
                    navigator.goBack()
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
                },
                onNotificationsClick = {
                    navigator.navigate(Notifications)
                },
                onProfileClick = {
                    navigator.navigate(StudentAccountProfile)
                },
                onLogoutClick = {
                    authViewModel.logout()
                    navigator.navigate(Login)
                }
            )
        }

        entry<StudentAccountProfile> {
            StudentAccountProfileScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<SubmitResearch> {
            SubmissionScreen(
                onSubmissionSuccess = {
                    navigator.navigate(StudentDashboard)
                },
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<SubmissionStatus> {
            SubmissionStatusScreen(
                onSubmissionClick = { submissionId ->
                    navigator.navigate(
                        StudentSubmissionDetails(submissionId)
                    )
                },
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<StudentSubmissionDetails> {
            StudentSubmissionDetailsScreen(
                submissionId = it.submissionId,
                onResubmitClick = { submissionId, currentVersion ->
                    navigator.navigate(
                        Resubmission(
                            submissionId = submissionId,
                            currentVersion = currentVersion
                        )
                    )
                },
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<Resubmission> {
            ResubmissionScreen(
                submissionId = it.submissionId,
                currentVersion = it.currentVersion,
                onResubmissionSuccess = {
                    navigator.goBack()
                },
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<SupervisorDashboard> {
            SupervisorDashboardScreen(
                onViewStudentsClick = {
                    navigator.navigate(SupervisorStudents)
                },
                onViewSubmissionsClick = {
                    navigator.navigate(SupervisorSubmissions)
                },
                onNotificationsClick = {
                    navigator.navigate(Notifications)
                },
                onProfileClick = {
                    navigator.navigate(SupervisorAccountProfile)
                },
                onLogoutClick = {
                    authViewModel.logout()
                    navigator.navigate(Login)
                }
            )
        }

        entry<SupervisorAccountProfile> {
            SupervisorAccountProfileScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<SupervisorStudents> {
            SupervisorStudentsScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<SupervisorSubmissions> {
            SupervisorSubmissionsScreen(
                onBackClick = {
                    navigator.goBack()
                },
                onSubmissionClick = { submissionId ->
                    navigator.navigate(
                        SubmissionDetails(submissionId)
                    )
                }
            )
        }

        entry<SubmissionDetails> {
            SubmissionDetailsScreen(
                submissionId = it.submissionId,
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<AdminDashboard> {
            AdminDashboardScreen(
                onSupervisorAssignmentClick = {
                    navigator.navigate(SupervisorAssignment)
                },
                onManageUsersClick = {
                    navigator.navigate(ManageUsers)
                },
                onResearchArchiveClick = {
                    navigator.navigate(AdminResearchArchive)
                },
                onReportsClick = {
                    navigator.navigate(AdminReports)
                },
                onLogoutClick = {
                    authViewModel.logout()
                    navigator.navigate(Login)
                }
            )
        }

        entry<SupervisorAssignment> {
            AdminAssignmentScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<ManageUsers> {
            AdminUsersScreen(
                onBackClick = {
                    navigator.goBack()
                },
                onCreateUserClick = {
                    navigator.navigate(CreateUser)
                }
            )
        }

        entry<AdminResearchArchive> {
            AdminResearchArchiveScreen(
                onBackClick = {
                    navigator.goBack()
                },
                onArchiveClick = { archiveId ->
                    navigator.navigate(
                        AdminResearchArchiveDetails(
                            archiveId = archiveId
                        )
                    )
                }
            )
        }

        entry<AdminResearchArchiveDetails> { route ->
            AdminResearchArchiveDetailsScreen(
                archiveId = route.archiveId,
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<AdminReports> {
            AdminReportsScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

        entry<CreateUser> {
            AdminCreateUserScreen(
                onBackClick = {
                    navigator.goBack() },
                onUserCreated = {
                    navigator.goBack()
                }
            )
        }

        entry<Notifications> {
            NotificationScreen(
                onBackClick = {
                    navigator.goBack()
                }
            )
        }

    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = {
            val currentStack =
                navigationState.backStacks[navigationState.topLevelRoute]

            if (currentStack != null && currentStack.size > 1) {
                navigator.goBack()
            } else {
                activity?.finish()
            }
        }
    )
}