package com.octopus.service

import com.octopus.domain.Exercise
import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType
import com.octopus.domain.NewExercise
import com.octopus.domain.repository.IExerciseRepository
import com.octopus.exception.NotFoundException

class ExerciseService(private val exerciseRepository: IExerciseRepository) {
    suspend fun create(exercise: NewExercise): Exercise {
        return exerciseRepository.create(exercise)
    }

    suspend fun getAll(): List<Exercise> {
        return exerciseRepository.findAll()
    }

    suspend fun getById(id: Long): Exercise {
        return exerciseRepository.findById(id) ?: throw NotFoundException("Exercise not found with id: $id")
    }

    suspend fun getAllByType(type: ExerciseType): List<Exercise> {
        return exerciseRepository.findByType(type)
    }

    suspend fun getAllByDifficulty(difficulty: ExerciseDifficulty): List<Exercise> {
        return exerciseRepository.findByDifficulty(difficulty)
    }
}