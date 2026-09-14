package com.researchflow.app.di

import com.researchflow.app.data.repository.AdminUserRepositoryImpl
import com.researchflow.app.data.repository.AuthRepositoryImpl
import com.researchflow.app.data.repository.DeadlineRepositoryImpl
import com.researchflow.app.data.repository.FeedbackRepositoryImpl
import com.researchflow.app.data.repository.NotificationRepositoryImpl
import com.researchflow.app.data.repository.ResearchArchiveRepositoryImpl
import com.researchflow.app.data.repository.StudentRepositoryImpl
import com.researchflow.app.data.repository.SubmissionRepositoryImpl
import com.researchflow.app.data.repository.SubmissionVersionRepositoryImpl
import com.researchflow.app.data.repository.UserRepositoryImpl
import com.researchflow.app.domain.repository.AdminUserRepository
import com.researchflow.app.domain.repository.AuthRepository
import com.researchflow.app.domain.repository.DeadlineRepository
import com.researchflow.app.domain.repository.FeedbackRepository
import com.researchflow.app.domain.repository.NotificationRepository
import com.researchflow.app.domain.repository.ResearchArchiveRepository
import com.researchflow.app.domain.repository.StudentRepository
import com.researchflow.app.domain.repository.SubmissionRepository
import com.researchflow.app.domain.repository.SubmissionVersionRepository
import com.researchflow.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        implementation: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        implementation: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindStudentRepository(
        implementation: StudentRepositoryImpl
    ): StudentRepository

    @Binds
    @Singleton
    abstract fun bindSubmissionRepository(
        implementation: SubmissionRepositoryImpl
    ): SubmissionRepository

    @Binds
    @Singleton
    abstract fun bindSubmissionVersionRepository(
        implementation: SubmissionVersionRepositoryImpl
    ): SubmissionVersionRepository

    @Binds
    abstract fun bindAdminUserRepository(
        implementation: AdminUserRepositoryImpl
    ): AdminUserRepository

    @Binds
    abstract fun bindFeedbackRepository(
        implementation: FeedbackRepositoryImpl
    ): FeedbackRepository

    @Binds
    abstract fun bindDeadlineRepository(
        implementation: DeadlineRepositoryImpl
    ): DeadlineRepository

    @Binds
    abstract fun bindNotificationRepository(
        implementation: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    abstract fun bindResearchArchiveRepository(
        impl: ResearchArchiveRepositoryImpl
    ): ResearchArchiveRepository
}