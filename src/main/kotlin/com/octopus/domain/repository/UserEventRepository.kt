package com.octopus.domain.repository

import com.octopus.domain.UserEvent
import com.octopus.domain.UserEvents
import com.octopus.exception.WrongArgumentException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

class UserEventRepository : IUserEventRepository {

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        transaction {
            SchemaUtils.create(UserEvents)
        }
    }

    override fun create(userId: UUID, exerciseId: Long): UserEvent {
        log.debug("[create] userId: $userId, exerciseId: $exerciseId")

        val now = Instant.now().epochSecond
        val userEvent = UserEvent(
            userId = userId, exerciseId = exerciseId,
            createdAt = now, updatedAt = now, progress = 0, isCompleted = false
        )
        return transaction {
            userEvent.copy(
                id = UserEvents.insertAndGetId { row ->
                    row[this.userId] = userEvent.userId
                    row[this.exerciseId] = userEvent.exerciseId
                    row[this.createdAt] = userEvent.createdAt
                    row[this.updatedAt] = userEvent.updatedAt
                    row[this.progress] = userEvent.progress
                    row[this.isCompleted] = userEvent.isCompleted
                }.value
            )
        }
    }

    override fun update(userEvent: UserEvent): UserEvent? {
        log.debug("[update] userEvent: $userEvent")

        val id = userEvent.id ?: throw WrongArgumentException("Event Id can not be null")

        transaction {
            UserEvents.update({ UserEvents.id eq id }) { row ->
                row[this.updatedAt] = Instant.now().epochSecond
                row[this.progress] = userEvent.progress
                row[this.isCompleted] = userEvent.isCompleted
            }
        }
        return findEventById(id)
    }

    override fun findEventById(eventId: UUID): UserEvent? {
        log.debug("[findEventById] eventId: $eventId")

        return transaction {
            UserEvents
                .select { UserEvents.id eq eventId }
                .map { UserEvents.toUserEvent(it) }
                .firstOrNull()
        }
    }

    override fun findUserEvents(userId: UUID): List<UserEvent> {
        log.debug("[findUserEvents] userId: $userId")

        return transaction {
            UserEvents
                .select { UserEvents.userId eq userId }
                .map { UserEvents.toUserEvent(it) }
        }
    }
}