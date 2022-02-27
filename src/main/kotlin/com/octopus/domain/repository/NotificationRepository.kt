package com.octopus.domain.repository

import com.octopus.domain.Notification
import com.octopus.domain.Notifications
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface INotificationRepository {
    fun create(userId: UUID, message: String): UUID
    fun batchCreate(notifications: List<Notification>)
    fun findAll(): List<Notification>
}

class NotificationRepository : INotificationRepository {
    init {
        transaction{ SchemaUtils.create(Notifications) }
    }

    override fun create(userId: UUID, message: String): UUID {
        return transaction {
            Notifications.insertAndGetId { row ->
                row[this.userId] = userId
                row[this.message] = message
            }.value
        }
    }

    override fun batchCreate(notifications: List<Notification>) {
        transaction {
            Notifications.batchInsert(notifications) { item: Notification ->
                this[Notifications.userId] = item.userId
                this[Notifications.message] = item.message

            }
        }
    }

    override fun findAll(): List<Notification> {
        return transaction {
            Notifications
                .selectAll()
                .map { Notifications.toNotification(it) }
        }
    }

}