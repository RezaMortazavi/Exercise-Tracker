package com.octopus.domain

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object Notifications : UUIDTable() {
    val userId = uuid("user_id").references(Users.id).index()
    val createTime = datetime("create_time").clientDefault { LocalDateTime.now() }
    val message = text("message")
}

fun Notifications.toNotification(row: ResultRow) : Notification {
    return Notification(
        id = row[id].value,
        userId = row[userId],
        createTime = row[createTime],
        message = row[message],
    )
}

data class Notification(
    val id: UUID,
    val userId: UUID,
    val createTime: LocalDateTime? = null,
    val message: String
)

data class NewNotification(
    val userId: UUID,
    val createTime: LocalDateTime? = null,
    val message: String
)