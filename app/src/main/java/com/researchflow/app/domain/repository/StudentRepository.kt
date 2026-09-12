package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.Student

interface StudentRepository {

    suspend fun createStudent(student: Student)

    suspend fun getStudent(studentId: String): Student?

    suspend fun getStudentsBySupervisor(
        supervisorId: String
    ): List<Student>

    suspend fun getAllStudents(): List<Student>

    suspend fun assignSupervisor(
        studentId: String,
        supervisorId: String
    )
}