package com.researchflow.app.domain.usecase.student

import com.researchflow.app.data.model.Student
import com.researchflow.app.domain.repository.StudentRepository
import javax.inject.Inject

class CreateStudentUseCase @Inject constructor(
    private val studentRepository: StudentRepository
) {

    suspend operator fun invoke(student: Student) {
        studentRepository.createStudent(student)
    }
}