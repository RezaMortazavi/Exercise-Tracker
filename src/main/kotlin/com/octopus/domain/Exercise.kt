package com.octopus.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

object Exercises : LongIdTable() {
    val title = varchar("title", 50)
    val description = varchar("description", 255)
    val thumbnailUrl = varchar("thumbnail_url", 100).nullable()
    val videoUrl = varchar("video_url", 100).nullable()
    val duration = long("duration")
    val type = enumerationByName("type", 20, ExerciseType::class)
    val difficulty = enumerationByName("difficulty", 20, ExerciseDifficulty::class)

    fun toExercise(row: ResultRow): Exercise {
        return Exercise(row[id].value,
            row[title],
            row[description],
            row[thumbnailUrl],
            row[videoUrl],
            row[duration],
            row[type],
            row[difficulty])
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