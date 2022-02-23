package com.octopus.domain.repository

import com.octopus.domain.*
import com.octopus.exception.WrongArgumentException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*


interface IUserEventRepository {
    fun create(userId: UUID, exercise: ExerciseDao): UserEvent
    fun update(userEvent: UserEvent): UserEvent?
    fun findEventById(eventId: UUID): UserEvent?
    fun findUserEvents(userId: UUID): List<UserEvent>
}

class UserEventRepository : IUserEventRepository {

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        transaction {
            SchemaUtils.create(UserEvents)
        }
    }

    override fun create(userId: UUID, exercise: ExerciseDao): UserEvent {
        log.debug("[create] userId: $userId, exerciseId: ${exercise.id}")

        val now = Instant.now().epochSecond

        return transaction {
            UserEventEntity.new {
                this.userId = userId
                this.exercise = exercise
                this.createdAt = now
                this.updatedAt = now
                this.progress = 0
                this.isCompleted = false
            }.toUserEvent()
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

        return transaction { UserEventEntity.findById(eventId)?.toUserEvent() }
    }

    override fun findUserEvents(userId: UUID): List<UserEvent> {
        log.debug("[findUserEvents] userId: $userId")

        return transaction { UserEventEntity.find { UserEvents.userId eq userId }.map { it.toUserEvent() } }
    }
}