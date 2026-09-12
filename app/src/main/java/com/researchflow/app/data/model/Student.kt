package com.researchflow.app.data.model

data class Student(
    val studentId: String = "",
    val userId: String = "",
    val matricNumber: String = "",
    val department: String = "",
    val supervisorId: String? = null
)