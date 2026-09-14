package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.Submission
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import javax.inject.Inject

class CreateSubmissionUseCase @Inject constructor(
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository
) {

    suspend operator fun invoke(submission: Submission) {

        val student = studentRepository.getStudent(
            submission.studentId
        ) ?: throw IllegalStateException(
            "Student profile not found"
        )

        val supervisorId = student.supervisorId
            ?: throw IllegalStateException(
                "A supervisor must be assigned before submitting research."
            )

        val submissionWithSupervisor = submission.copy(
            supervisorId = supervisorId
        )

        submissionRepository.createSubmission(
            submissionWithSupervisor
        )
    }
}