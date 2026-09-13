package com.researchflow.app.domain.usecase.user

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.Student
import com.researchflow.app.data.model.User
import com.researchflow.app.data.model.UserRole
import com.researchflow.app.domain.repository.AdminUserRepository
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val adminUserRepository: AdminUserRepository,
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        role: UserRole,
        matricNumber: String = "",
        department: String = ""
    ): String {

        // Create Firebase Authentication account
        val userId = adminUserRepository.createUserAccount(
            email = email,
            password = password
        )

        // Create users/{uid} document
        val user = User(
            userId = userId,
            name = name,
            email = email,
            role = role,
            createdAt = Timestamp.now()
        )

        userRepository.createUser(user)

        // Create students/{uid} document for student accounts
        if (role == UserRole.STUDENT) {
            val student = Student(
                studentId = userId,
                userId = userId,
                matricNumber = matricNumber,
                department = department
            )

            studentRepository.createStudent(student)
        }

        return userId
    }
}