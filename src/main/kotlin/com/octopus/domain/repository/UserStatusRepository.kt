package com.octopus.domain.repository

import com.octopus.domain.Exercises
import com.octopus.domain.UserStatus
import com.octopus.domain.UserStatuses
import com.octopus.domain.Users
import com.octopus.exception.WrongArgumentException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

interface IUserStatusRepository {
    fun create(userId: UUID): UserStatus
    fun update(userStatus: UserStatus): UserStatus?
    fun findById(id: Long): UserStatus?
    fun findByUserId(userId: UUID): UserStatus?
}

class UserStatusRepository : IUserStatusRepository {
    init {
        transaction {
            SchemaUtils.create(UserStatuses)
        }
    }

    override fun create(userId: UUID): UserStatus {
        val now = LocalDateTime.now()

        return transaction {
            val id = UserStatuses.insertAndGetId { row ->
                row[this.userId] = userId
                row[this.lastActivityAt] = now
            }.value

            findById(id)!!
        }
    }

    override fun update(userStatus: UserStatus): UserStatus? {
        val id = userStatus.id ?: throw WrongArgumentException("UserStatus id is missing")
        val now = LocalDateTime.now()

        transaction {
            UserStatuses.update({UserStatuses.id eq id}) { row ->
                row[this.lastActivityAt] = now
            }
        }

        return findById(id)
    }

    override fun findById(id: Long): UserStatus? {
        return transaction {
            UserStatuses
                .select(UserStatuses.id eq  id)
                .map {  UserStatuses.toUserStatus(it) }
                .firstOrNull()
        }
    }

    override fun findByUserId(userId: UUID): UserStatus? {
        return transaction {
            UserStatuses
                .select(UserStatuses.userId eq  userId)
                .map {  UserStatuses.toUserStatus(it) }
                .firstOrNull()
        }
    }
}