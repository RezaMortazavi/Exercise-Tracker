package com.octopus.domain

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID


object UserEvents : UUIDTable() {
    val userId = uuid("userId").references(Users.id).index() // points to User table
    val exerciseId = long("exerciseId").references(Exercises.id)   // points to Exercise table
    val createdAt = long("createdAt")
    val updatedAt = long("updatedAt")
    val progress = long("progress")
    val isCompleted = bool("isCompleted")

    fun toUserEvent(row: ResultRow): UserEvent {
        return UserEvent(
            id = row[id].value,
            userId = row[userId],
            exerciseId = row[exerciseId],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt],
            progress = row[progress],
            isCompleted = row[isCompleted]
        )
    }
}

data class UserEvent(
    val id: UUID? = null,
    val userId: UUID,
    val exerciseId: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val progress: Long,
    val isCompleted: Boolean
)

data class UserEventInput(
    val userId: UUID,
    val exerciseId: Long,
)