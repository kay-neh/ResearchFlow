package com.researchflow.app.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Login : NavKey

@Serializable
data object Register : NavKey

@Serializable
data object ForgotPassword : NavKey

@Serializable
data object StudentProfile : NavKey

@Serializable
data object StudentDashboard : NavKey

@Serializable
data object StudentAccountProfile : NavKey

@Serializable
data object SubmitResearch : NavKey

@Serializable
data object SubmissionStatus : NavKey

@Serializable
data class StudentSubmissionDetails(
    val submissionId: String
) : NavKey

@Serializable
data class Resubmission(
    val submissionId: String,
    val currentVersion: Int
) : NavKey

@Serializable
data object SupervisorDashboard : NavKey

@Serializable
data object SupervisorAccountProfile : NavKey

@Serializable
data object SupervisorStudents : NavKey

@Serializable
data object SupervisorSubmissions : NavKey

@Serializable
data class SubmissionDetails(
    val submissionId: String
) : NavKey

@Serializable
data object AdminDashboard : NavKey

@Serializable
data object SupervisorAssignment : NavKey

@Serializable
data object ManageUsers : NavKey

@Serializable
data object AdminResearchArchive : NavKey

@Serializable
data class AdminResearchArchiveDetails(
    val archiveId: String
) : NavKey

@Serializable
data object AdminReports : NavKey

@Serializable
data object CreateUser : NavKey

@Serializable
data object Notifications : NavKey