package com.octopus.service

import com.octopus.domain.Exercise
import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType
import com.octopus.domain.repository.IExerciseRepository
import com.octopus.exception.NotFoundException

class ExerciseService(private val exerciseRepository: IExerciseRepository) {
    fun create(exercise: Exercise): Exercise {
        return exerciseRepository.create(exercise).toExercise()
    }

    fun getAll(): List<Exercise> {
        return exerciseRepository.findAll().map { it.toExercise() }
    }

    fun getById(id: Long): Exercise {
        return exerciseRepository.findById(id)?.toExercise() ?: throw NotFoundException("Exercise not found with id: $id")
    }

    fun getAllByType(type: ExerciseType): List<Exercise> {
        return exerciseRepository.findByType(type).map { it.toExercise() }
    }

    fun getAllByDifficulty(difficulty: ExerciseDifficulty): List<Exercise> {
        return exerciseRepository.findByDifficulty(difficulty).map { it.toExercise() }
    }
}