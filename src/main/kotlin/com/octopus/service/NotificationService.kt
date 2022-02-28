package com.octopus.service

import com.octopus.domain.NewNotification
import com.octopus.domain.User
import com.octopus.domain.repository.INotificationRepository
import org.slf4j.LoggerFactory

class NotificationService(private val repository: INotificationRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Send notification and save a record in notifications table
     * Now it just logs a message but in real example we could have used Push Notification server to handle this
     */
    suspend fun sendNotification(users: List<User>) {
        val notifications = generateNotifications(users)
        log.info("sending notifications: $notifications")
        batchSaveNotification(notifications)
    }

    private fun generateNotifications(users: List<User>): List<NewNotification> {
        val notifications = users.map {
            val message = "${it.firstName}, It's almost time to exercise!"
            NewNotification(userId = it.id, message = message)
        }

        return notifications
    }

    private suspend fun batchSaveNotification(notifications: List<NewNotification>) {
        repository.batchCreate(notifications)
    }
}