package com.researchflow.app.domain.usecase.admin

import com.researchflow.app.data.model.AdminReport
import com.researchflow.app.data.model.SubmissionStatus
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.domain.repository.UserRepository
import javax.inject.Inject

class GetAdminReportUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val submissionRepository: SubmissionRepository
) {

    suspend operator fun invoke(): AdminReport {

        val users = userRepository.getAllUsers()
        val students = studentRepository.getAllStudents()
        val supervisors = userRepository.getSupervisors()

        // Collect submissions for all students.
        val submissions = students.flatMap { student ->
            submissionRepository.getSubmissionsByStudent(
                student.studentId
            )
        }.distinctBy { it.submissionId }

        return AdminReport(
            totalUsers = users.size,
            totalStudents = students.size,
            totalSupervisors = supervisors.size,
            totalSubmissions = submissions.size,

            submitted = submissions.count {
                it.status == SubmissionStatus.SUBMITTED
            },

            underReview = submissions.count {
                it.status == SubmissionStatus.UNDER_REVIEW
            },

            correctionRequired = submissions.count {
                it.status == SubmissionStatus.CORRECTION_REQUIRED
            },

            resubmitted = submissions.count {
                it.status == SubmissionStatus.RESUBMITTED
            },

            approved = submissions.count {
                it.status == SubmissionStatus.APPROVED
            },

            rejected = submissions.count {
                it.status == SubmissionStatus.REJECTED
            }
        )
    }
}