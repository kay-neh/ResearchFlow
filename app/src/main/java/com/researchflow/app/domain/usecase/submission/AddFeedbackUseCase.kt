package com.researchflow.app.domain.usecase.submission

import com.google.firebase.Timestamp
import com.researchflow.app.data.model.Feedback
import com.researchflow.app.domain.repository.FeedbackRepository
import java.util.UUID
import javax.inject.Inject

class AddFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {

    suspend operator fun invoke(
        submissionId: String,
        versionId: String,
        supervisorId: String,
        comment: String
    ) {
        if (comment.isBlank()) {
            throw IllegalArgumentException("Feedback cannot be empty")
        }

        if (versionId.isBlank()) {
            throw IllegalArgumentException("Submission version is required")
        }

        val feedback = Feedback(
            feedbackId = UUID.randomUUID().toString(),
            submissionId = submissionId,
            versionId = versionId,
            supervisorId = supervisorId,
            comment = comment.trim(),
            createdAt = Timestamp.now()
        )

        feedbackRepository.createFeedback(feedback)
    }
}