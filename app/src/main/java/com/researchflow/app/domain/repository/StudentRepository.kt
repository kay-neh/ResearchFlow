package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.Student

interface StudentRepository {

    suspend fun createStudent(student: Student)

    suspend fun getStudent(studentId: String): Student?
}