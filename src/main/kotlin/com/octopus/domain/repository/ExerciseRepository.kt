package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory.dbQuery
import com.octopus.domain.*
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll


interface IExerciseRepository {
    suspend fun create(exercise: NewExercise): Exercise
    suspend fun findById(id: Long): Exercise?
    suspend fun findAll(): List<Exercise>
    suspend fun findByType(type: ExerciseType): List<Exercise>
    suspend fun findByDifficulty(difficulty: ExerciseDifficulty): List<Exercise>
}

class ExerciseRepository : IExerciseRepository {

    override suspend fun create(exercise: NewExercise): Exercise {

        val id = dbQuery {
            Exercises.insertAndGetId { row ->
                row[title] = exercise.title
                row[description] = exercise.description
                row[duration] = exercise.duration
                row[type] = exercise.type
                row[difficulty] = exercise.difficulty
            }.value
        }

        return findById(id)!!
    }

    override suspend fun findById(id: Long): Exercise? = dbQuery {
        Exercises
            .select { Exercises.id eq id }
            .map { Exercises.toExercise(it) }
            .firstOrNull()
    }

    override suspend fun findAll(): List<Exercise> = dbQuery {
        Exercises
            .selectAll()
            .map { Exercises.toExercise(it) }

    }

    override suspend fun findByType(type: ExerciseType): List<Exercise> = dbQuery {
        Exercises
            .select { Exercises.type eq type }
            .map { Exercises.toExercise(it) }

    }

    override suspend fun findByDifficulty(difficulty: ExerciseDifficulty): List<Exercise> = dbQuery {
        Exercises
            .select { Exercises.difficulty eq difficulty }
            .map { Exercises.toExercise(it) }
    }
}