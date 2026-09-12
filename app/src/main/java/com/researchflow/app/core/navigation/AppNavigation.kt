package com.researchflow.app.core.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.researchflow.app.presentation.auth.LoginScreen
import com.researchflow.app.presentation.auth.RegisterScreen
import com.researchflow.app.presentation.student.StudentDashboardScreen
import com.researchflow.app.presentation.student.StudentProfileScreen
import com.researchflow.app.presentation.student.SubmissionScreen

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
                onLoginSuccess = {
                    navigator.navigate(StudentDashboard)
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

    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = {
            navigator.goBack()
        }
    )
}