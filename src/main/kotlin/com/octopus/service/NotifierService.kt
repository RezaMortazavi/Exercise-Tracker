package com.octopus.service

import com.octopus.domain.Notifier
import com.octopus.domain.User
import com.octopus.domain.repository.INotifierRepository
import org.slf4j.LoggerFactory
import java.util.*

class NotifierService(private val repository: INotifierRepository, private val notificationService: NotificationService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun createNotifiersForUer(notifiers: List<Notifier>) {
        log.debug("[createNotifiersForUer] notifiers: $notifiers")
        repository.batchCreate(notifiers)
    }

    fun notifyEligibleUsers() {
        val notifiableUsers = findNotifiableUsers()
        if (notifiableUsers.isEmpty()) {
            log.debug("No registered users to be notified")
        } else {
            sendNotificationToUsers(notifiableUsers)
        }
    }

    private fun sendNotificationToUsers(users: List<User>) = notificationService.sendNotification(users)

    /**
     * Users registered to receive notification for current day and hour but not active today
     */
    private fun findNotifiableUsers() = repository.findRegisteredUsersNotActiveToday(getCurrentDayOfWeek(), getCurrentHour())

    private fun getCurrentHour() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    private fun getCurrentDayOfWeek() = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
}