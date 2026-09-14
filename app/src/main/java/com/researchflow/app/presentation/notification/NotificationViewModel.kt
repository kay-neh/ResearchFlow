package com.researchflow.app.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.researchflow.app.data.model.Notification
import com.researchflow.app.domain.usecase.notification.GetUserNotificationsUseCase
import com.researchflow.app.domain.usecase.notification.MarkNotificationAsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getUserNotificationsUseCase: GetUserNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(NotificationUiState())

    val uiState: StateFlow<NotificationUiState> =
        _uiState.asStateFlow()

    fun loadNotifications() {
        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            try {
                val userId =
                    firebaseAuth.currentUser?.uid
                        ?: throw IllegalStateException(
                            "User is not logged in"
                        )

                val notifications =
                    getUserNotificationsUseCase(userId)

                _uiState.value =
                    NotificationUiState(
                        notifications = notifications
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage =
                            e.message
                                ?: "Failed to load notifications"
                    )
            }
        }
    }

    fun markAsRead(
        notificationId: String
    ) {
        viewModelScope.launch {

            try {

                markNotificationAsReadUseCase(
                    notificationId
                )

                _uiState.value =
                    _uiState.value.copy(
                        notifications =
                            _uiState.value.notifications.map { notification ->

                                if (
                                    notification.notificationId ==
                                    notificationId
                                ) {
                                    notification.copy(
                                        isRead = true
                                    )
                                } else {
                                    notification
                                }
                            },
                        errorMessage = null
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        errorMessage =
                            e.message
                                ?: "Failed to mark notification as read"
                    )
            }
        }
    }

    fun refresh() {
        loadNotifications()
    }
}