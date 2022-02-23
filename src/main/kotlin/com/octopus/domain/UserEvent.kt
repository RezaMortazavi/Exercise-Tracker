package com.octopus.domain

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID


object UserEvents : UUIDTable() {
    val userId = uuid("userId").references(Users.id).index() // points to User table
    val exercise = reference("exercise", Exercises)   // points to Exercise table
    val createdAt = long("createdAt")
    val updatedAt = long("updatedAt")
    val progress = long("progress")
    val isCompleted = bool("isCompleted")
}

class UserEventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEventEntity>(UserEvents)

    var userId by UserEvents.userId
    var exercise by ExerciseDao referencedOn UserEvents.exercise
    var createdAt by UserEvents.createdAt
    var updatedAt by UserEvents.updatedAt
    var progress by UserEvents.progress
    var isCompleted by UserEvents.isCompleted

    fun toUserEvent(): UserEvent{
        return UserEvent(id.value,
            userId,
            exercise.toExercise(),
            createdAt,
            updatedAt,
            progress,
            isCompleted)
    }
}

data class UserEvent(
    val id: UUID? = null,
    val userId: UUID,
    val exercise: Exercise,
    val createdAt: Long,
    val updatedAt: Long,
    val progress: Long,
    val isCompleted: Boolean
)

data class UserEventInput(
    val userId: UUID,
    val exerciseId: Long,
)

data class UpdateUserEventInput(
    val progress: Long
)