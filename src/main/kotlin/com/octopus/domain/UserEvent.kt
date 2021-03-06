package com.octopus.domain

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*


object UserEvents : UUIDTable() {

    val userId = uuid("user_id").references(Users.id).index()  // FK Users Table
    val exerciseId = long("exercise_id").references(Exercises.id).index()  // FK Exercise Table
    val createTime = datetime("create_time").clientDefault { LocalDateTime.now() }
    val updateTime = datetime("update_time").clientDefault { LocalDateTime.now() }
    val progress = long("progress").clientDefault { 0 }
    val isCompleted = bool("isCompleted").clientDefault { false }
}

fun UserEvents.toUserEvent(row: ResultRow) : UserEvent {
    return UserEvent(row[id].value,
        row[userId],
        row[exerciseId],
        row[createTime],
        row[updateTime],
        row[progress],
        row[isCompleted])
}

data class UserEvent(
    val id: UUID,
    val userId: UUID,
    val exerciseId: Long,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime,
    val progress: Long,
    val isCompleted: Boolean
)