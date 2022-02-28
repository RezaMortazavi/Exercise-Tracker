package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory.dbQuery
import com.octopus.domain.NewNotification
import com.octopus.domain.Notification
import com.octopus.domain.Notifications
import com.octopus.domain.toNotification
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import java.util.*

interface INotificationRepository {
    suspend fun create(userId: UUID, message: String): UUID
    suspend fun batchCreate(notifications: List<NewNotification>)
    suspend fun findAll(): List<Notification>
}

class NotificationRepository : INotificationRepository {

    override suspend fun create(userId: UUID, message: String): UUID = dbQuery {
        Notifications.insertAndGetId { row ->
            row[this.userId] = userId
            row[this.message] = message
        }.value

    }

    override suspend fun batchCreate(notifications: List<NewNotification>) {
        dbQuery {
            Notifications.batchInsert(notifications) { item: NewNotification ->
                this[Notifications.userId] = item.userId
                this[Notifications.message] = item.message

            }
        }
    }

    override suspend fun findAll(): List<Notification> = dbQuery {
        Notifications
            .selectAll()
            .map { Notifications.toNotification(it) }

    }
}