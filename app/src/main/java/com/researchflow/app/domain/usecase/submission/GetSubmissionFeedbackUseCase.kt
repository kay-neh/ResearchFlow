package com.researchflow.app.domain.usecase.submission

import com.researchflow.app.data.model.Feedback
import com.researchflow.app.domain.repository.FeedbackRepository
import javax.inject.Inject

class GetSubmissionFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {

    suspend operator fun invoke(
        submissionId: String
    ): List<Feedback> {
        return feedbackRepository.getFeedbackBySubmission(
            submissionId
        )
    }

    suspend fun byVersion(
        versionId: String
    ): List<Feedback> {
        return feedbackRepository.getFeedbackByVersion(
            versionId
        )
    }
}