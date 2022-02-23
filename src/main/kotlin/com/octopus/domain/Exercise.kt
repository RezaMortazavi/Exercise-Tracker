package com.octopus.domain

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Exercises : LongIdTable() {
    val title = varchar("title", 50)
    val description = varchar("description", 255)
    val thumbnailUrl = varchar("thumbnailUrl", 100).nullable()
    val videoUrl = varchar("videoUrl", 100).nullable()
    val duration = long("duration")
    val type = enumerationByName("type", 20, ExerciseType::class)
    val difficulty = enumerationByName("difficulty", 20, ExerciseDifficulty::class)
}

class ExerciseDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ExerciseDao>(Exercises)

    var title by Exercises.title
    var description by Exercises.description
    var thumbnailUrl by Exercises.thumbnailUrl
    var videoUrl by Exercises.videoUrl
    var duration by Exercises.duration
    var type by Exercises.type
    var difficulty by Exercises.difficulty

    fun toExercise(): Exercise {
        return Exercise(id.value, title, description, thumbnailUrl, videoUrl, duration, type, difficulty)
    }
}

data class Exercise(
    val id: Long? = null,
    val title: String,
    val description: String,
    val thumbnailUrl: String? = null,
    val videoUrl: String? = null,
    val duration: Long,
    val type: ExerciseType,
    val difficulty: ExerciseDifficulty,
)

data class ExerciseInput(
    val title: String,
    val description: String,
    val duration: Long,
    val type: ExerciseType,
    val difficulty: ExerciseDifficulty
)

enum class ExerciseType {
    CARDIO,
    YOGA,
    PILATES,
    STRENGTH,
    STRETCH
}

enum class ExerciseDifficulty {
    EASY,
    MODERATE,
    HARD
}