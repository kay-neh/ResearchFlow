package com.researchflow.app.data.model

data class AdminReport(
    val totalUsers: Int = 0,
    val totalStudents: Int = 0,
    val totalSupervisors: Int = 0,
    val totalSubmissions: Int = 0,
    val submitted: Int = 0,
    val underReview: Int = 0,
    val correctionRequired: Int = 0,
    val resubmitted: Int = 0,
    val approved: Int = 0,
    val rejected: Int = 0
)