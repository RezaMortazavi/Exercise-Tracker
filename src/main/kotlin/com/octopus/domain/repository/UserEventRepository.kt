package com.octopus.domain.repository

import com.octopus.domain.UserEvent
import com.octopus.domain.UserEvents
import com.octopus.exception.WrongArgumentException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.util.*


interface IUserEventRepository {
    fun create(userId: UUID, exerciseId: Long): UserEvent
    fun update(userEvent: UserEvent): UserEvent?
    fun findEventById(eventId: UUID): UserEvent?
    fun findUserEvents(userId: UUID): List<UserEvent>
}

class UserEventRepository : IUserEventRepository {
    init {
        transaction {
            SchemaUtils.create(UserEvents)
        }
    }
    override fun create(userId: UUID, exerciseId: Long): UserEvent {
        return transaction {
            val id = UserEvents.insertAndGetId { row ->
                row[this.userId] = userId
                row[this.exerciseId] = exerciseId
            }.value

            findEventById(id)!!
        }
    }

    override fun update(userEvent: UserEvent): UserEvent? {
        val id = userEvent.id ?: throw WrongArgumentException("UserStatus id is missing")
        val now = LocalDateTime.now()

        transaction {
            UserEvents.update({ UserEvents.id eq id}) { row ->
                row[this.updateTime] = now
                row[this.progress] = userEvent.progress
                row[this.isCompleted] = userEvent.isCompleted
            }
        }

        return findEventById(id)
    }

    override fun findEventById(eventId: UUID): UserEvent? {
        return transaction {
            UserEvents
                .select { UserEvents.id eq eventId}
                .map { UserEvents.toUserEvent(it) }
                .firstOrNull()
        }
    }

    override fun findUserEvents(userId: UUID): List<UserEvent> {
        return transaction {
            UserEvents
                .select { UserEvents.userId eq userId}
                .map { UserEvents.toUserEvent(it) }
        }
    }

}