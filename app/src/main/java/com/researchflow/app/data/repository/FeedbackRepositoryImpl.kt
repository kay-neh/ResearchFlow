package com.researchflow.app.data.repository

import com.researchflow.app.data.firebase.FirestoreFeedbackService
import com.researchflow.app.data.model.Feedback
import com.researchflow.app.domain.repository.FeedbackRepository
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val firestoreFeedbackService: FirestoreFeedbackService
) : FeedbackRepository {

    override suspend fun createFeedback(
        feedback: Feedback
    ) {
        firestoreFeedbackService.createFeedback(feedback)
    }

    override suspend fun getFeedback(
        feedbackId: String
    ): Feedback? {
        return firestoreFeedbackService.getFeedback(
            feedbackId
        )
    }

    override suspend fun getFeedbackBySubmission(
        submissionId: String
    ): List<Feedback> {
        return firestoreFeedbackService.getFeedbackBySubmission(
            submissionId
        )
    }

    override suspend fun getFeedbackByVersion(
        versionId: String
    ): List<Feedback> =
        firestoreFeedbackService.getFeedbackByVersion(versionId)
}