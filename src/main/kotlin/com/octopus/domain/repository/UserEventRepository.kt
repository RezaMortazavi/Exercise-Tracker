package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory.dbQuery
import com.octopus.domain.UserEvent
import com.octopus.domain.UserEvents
import com.octopus.domain.toUserEvent
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.util.*


interface IUserEventRepository {
    suspend fun create(userId: UUID, exerciseId: Long): UserEvent
    suspend fun update(userEvent: UserEvent): UserEvent?
    suspend fun findEventById(eventId: UUID): UserEvent?
    suspend fun findUserEvents(userId: UUID): List<UserEvent>
}

class UserEventRepository : IUserEventRepository {

    override suspend fun create(userId: UUID, exerciseId: Long): UserEvent {
        val id = dbQuery {
            UserEvents.insertAndGetId { row ->
                row[this.userId] = userId
                row[this.exerciseId] = exerciseId
            }.value
        }

        return findEventById(id)!!
    }

    override suspend fun update(userEvent: UserEvent): UserEvent? {
        val id = userEvent.id
        val now = LocalDateTime.now()

        dbQuery {
            UserEvents.update({ UserEvents.id eq id }) { row ->
                row[this.updateTime] = now
                row[this.progress] = userEvent.progress
                row[this.isCompleted] = userEvent.isCompleted
            }
        }

        return findEventById(id)
    }

    override suspend fun findEventById(eventId: UUID): UserEvent? = dbQuery {
        UserEvents
            .select { UserEvents.id eq eventId }
            .map { UserEvents.toUserEvent(it) }
            .firstOrNull()

    }

    override suspend fun findUserEvents(userId: UUID): List<UserEvent> = dbQuery {
        UserEvents
            .select { UserEvents.userId eq userId }
            .map { UserEvents.toUserEvent(it) }

    }
}