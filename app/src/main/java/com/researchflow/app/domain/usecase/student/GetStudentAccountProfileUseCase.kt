package com.researchflow.app.domain.usecase.student

import com.researchflow.app.domain.model.StudentAccountProfile
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class GetStudentAccountProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository
) {

    suspend operator fun invoke(
        userId: String
    ): StudentAccountProfile {

        val user = userRepository.getUser(userId)
            ?: throw IllegalStateException("User profile not found")

        val student = studentRepository.getStudentByUserId(userId)
            ?: throw IllegalStateException("Student profile not found")

        val supervisorName = student.supervisorId
            ?.let { supervisorId ->
                userRepository.getUser(supervisorId)?.name
            }

        return StudentAccountProfile(
            userId = user.userId,
            name = user.name,
            email = user.email,
            role = user.role,
            profilePictureUrl = user.profilePictureUrl,
            isActive = user.isActive,
            createdAt = user.createdAt,
            matricNumber = student.matricNumber,
            department = student.department,
            supervisorId = student.supervisorId,
            supervisorName = supervisorName
        )
    }
}