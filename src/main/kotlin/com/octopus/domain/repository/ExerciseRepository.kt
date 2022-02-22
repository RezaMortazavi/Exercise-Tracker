package com.octopus.domain.repository

import com.octopus.domain.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ExerciseRepository : IExerciseRepository {
    init {
        transaction {
            SchemaUtils.create(Exercises)
        }
    }

    override fun create(exercise: Exercise): Exercise {
        return transaction {
            exercise.copy(
                id = Exercises.insertAndGetId { row ->
                    row[title] = exercise.title
                    row[description] = exercise.description
                    row[duration] = exercise.duration
                    row[type] = exercise.type
                    row[difficulty] = exercise.difficulty
                }.value
            )
        }
    }

    override fun findById(id: Long): Exercise? {
        return transaction {
            Exercises
                .select { Exercises.id eq id }
                .map { Exercises.toExercise(it) }
                .firstOrNull()
        }
    }

    override fun findAll(): List<Exercise> {
        return transaction {
            Exercises
                .selectAll()
                .map { Exercises.toExercise(it) }
        }
    }

    override fun findByType(type: ExerciseType): List<Exercise> {
        return transaction {
            Exercises
                .select { Exercises.type eq type }
                .map { Exercises.toExercise(it) }
        }
    }

    override fun findByDifficulty(difficulty: ExerciseDifficulty): List<Exercise> {
        return transaction {
            Exercises
                .select { Exercises.difficulty eq difficulty }
                .map { Exercises.toExercise(it) }
        }
    }
}