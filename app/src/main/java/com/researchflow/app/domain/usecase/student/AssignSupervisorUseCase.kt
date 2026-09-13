package com.researchflow.app.domain.usecase.student

import com.researchflow.app.domain.repository.StudentRepository
import javax.inject.Inject

class AssignSupervisorUseCase @Inject constructor(
    private val studentRepository: StudentRepository
) {
    suspend operator fun invoke(
        studentId: String,
        supervisorId: String
    ) {
        studentRepository.assignSupervisor(
            studentId = studentId,
            supervisorId = supervisorId
        )
    }
}