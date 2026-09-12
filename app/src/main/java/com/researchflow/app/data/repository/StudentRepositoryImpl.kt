package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreStudentService
import com.researchflow.app.data.model.Student
import com.researchflow.app.domain.repository.StudentRepository
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val firestoreStudentService: FirestoreStudentService
) : StudentRepository {

    override suspend fun createStudent(student: Student) {
        firestoreStudentService.createStudent(student)
    }

    override suspend fun getStudent(studentId: String): Student? {
        return firestoreStudentService.getStudent(studentId)
    }

    override suspend fun getStudentsBySupervisor(
        supervisorId: String
    ): List<Student> {
        return firestoreStudentService.getStudentsBySupervisor(
            supervisorId
        )
    }

    override suspend fun getAllStudents(): List<Student> {
        return firestoreStudentService.getAllStudents()
    }

    override suspend fun assignSupervisor(
        studentId: String,
        supervisorId: String
    ) {
        firestoreStudentService.assignSupervisor(
            studentId = studentId,
            supervisorId = supervisorId
        )
    }
}