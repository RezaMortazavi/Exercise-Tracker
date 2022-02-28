package com.octopus.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object UserStatuses : LongIdTable() {
    val userId = uuid("user_id").references(Users.id).index()
    val lastActivityAt = datetime("last_activity_time")
}

fun UserStatuses.toUserStatus(row: ResultRow): UserStatus {
    return UserStatus(
        id = row[id].value,
        userId = row[userId],
        lastActivityAt = row[lastActivityAt]
    )
}

data class UserStatus(
    val id: Long,
    val userId: UUID,
    val lastActivityAt: LocalDateTime
)