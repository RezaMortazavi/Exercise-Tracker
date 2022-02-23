package com.octopus.domain.repository

import com.octopus.domain.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


interface IExerciseRepository {
    fun create(exercise: Exercise): ExerciseDao
    fun findById(id: Long): ExerciseDao?
    fun findAll(): List<ExerciseDao>
    fun findByType(type: ExerciseType): List<ExerciseDao>
    fun findByDifficulty(difficulty: ExerciseDifficulty): List<ExerciseDao>
}

class ExerciseRepository : IExerciseRepository {
    init {
        transaction {
            SchemaUtils.create(Exercises)
        }
    }

    override fun create(exercise: Exercise): ExerciseDao {
        return transaction {
            ExerciseDao.new {
                title = exercise.title
                description = exercise.description
                duration = exercise.duration
                type = exercise.type
                difficulty = exercise.difficulty
            }
        }
    }

    override fun findById(id: Long): ExerciseDao? {
        return transaction {
            ExerciseDao.findById(id)
        }
    }

    override fun findAll(): List<ExerciseDao> {
        return transaction {
            ExerciseDao.all().toList()
        }
    }

    override fun findByType(type: ExerciseType): List<ExerciseDao> {
        return transaction {
            ExerciseDao
                .find { Exercises.type eq type }
                .toList()
        }
    }

    override fun findByDifficulty(difficulty: ExerciseDifficulty): List<ExerciseDao> {
        return transaction {
            ExerciseDao
                .find { Exercises.difficulty eq difficulty }
                .toList()
        }
    }
}