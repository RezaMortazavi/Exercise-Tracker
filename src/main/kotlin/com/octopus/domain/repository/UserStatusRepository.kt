package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory.dbQuery
import com.octopus.domain.UserStatus
import com.octopus.domain.UserStatuses
import com.octopus.domain.toUserStatus
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime
import java.util.*

interface IUserStatusRepository {
    suspend fun create(userId: UUID): UserStatus
    suspend fun update(userStatus: UserStatus, overrideLastActiveTime: LocalDateTime? = null): UserStatus?
    suspend fun findById(id: Long): UserStatus?
    suspend fun findByUserId(userId: UUID): UserStatus?
}

class UserStatusRepository : IUserStatusRepository {

    override suspend fun create(userId: UUID): UserStatus {
        val now = LocalDateTime.now()

        val id = dbQuery {
            UserStatuses.insertAndGetId { row ->
                row[this.userId] = userId
                row[this.lastActivityAt] = now
            }.value
        }

        return findById(id)!!
    }

    override suspend fun update(userStatus: UserStatus, overrideLastActiveTime: LocalDateTime?): UserStatus? {
        val id = userStatus.id
        val lastActiveTime = overrideLastActiveTime ?: LocalDateTime.now()

        dbQuery {
            UserStatuses.update({ UserStatuses.id eq id }) { row ->
                row[this.lastActivityAt] = lastActiveTime
            }
        }

        return findById(id)
    }

    override suspend fun findById(id: Long): UserStatus? = dbQuery {
        UserStatuses
            .select(UserStatuses.id eq id)
            .map { UserStatuses.toUserStatus(it) }
            .firstOrNull()

    }

    override suspend fun findByUserId(userId: UUID): UserStatus? = dbQuery {
        UserStatuses
            .select(UserStatuses.userId eq userId)
            .map { UserStatuses.toUserStatus(it) }
            .firstOrNull()

    }
}