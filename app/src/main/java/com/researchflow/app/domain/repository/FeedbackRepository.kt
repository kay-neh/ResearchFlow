package com.researchflow.app.domain.repository

import com.researchflow.app.data.model.Feedback

interface FeedbackRepository {

    suspend fun createFeedback(feedback: Feedback)

    suspend fun getFeedback(feedbackId: String): Feedback?

    suspend fun getFeedbackBySubmission(
        submissionId: String
    ): List<Feedback>

    suspend fun getFeedbackByVersion(
        versionId: String
    ): List<Feedback>
}