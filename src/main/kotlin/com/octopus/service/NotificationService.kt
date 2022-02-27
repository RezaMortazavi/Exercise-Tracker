package com.octopus.service

import com.octopus.domain.Notification
import com.octopus.domain.User
import com.octopus.domain.repository.INotificationRepository
import org.slf4j.LoggerFactory

class NotificationService(private val repository: INotificationRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun sendNotification(users: List<User>) {
        val notifications = generateNotifications(users)
        log.debug("sending notifications: $notifications")
        batchSaveNotification(notifications)
    }

    private fun generateNotifications(users: List<User>): List<Notification> {
        val notifications = users.map {
            val message = "${it.firstName}, It's almost time to exercise!"
            Notification(userId = it.id!!, message = message)
        }

        return notifications
    }

    private fun batchSaveNotification(notifications: List<Notification>) {
        repository.batchCreate(notifications)
    }
}