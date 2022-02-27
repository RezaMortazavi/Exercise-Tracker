package com.octopus.service

import com.octopus.domain.Exercise
import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType
import com.octopus.domain.repository.IExerciseRepository
import com.octopus.exception.NotFoundException

class ExerciseService(private val exerciseRepository: IExerciseRepository) {
    fun create(exercise: Exercise): Exercise {
        return exerciseRepository.create(exercise)
    }

    fun getAll(): List<Exercise> {
        return exerciseRepository.findAll()
    }

    fun getById(id: Long): Exercise {
        return exerciseRepository.findById(id) ?: throw NotFoundException("Exercise not found with id: $id")
    }

    fun getAllByType(type: ExerciseType): List<Exercise> {
        return exerciseRepository.findByType(type)
    }

    fun getAllByDifficulty(difficulty: ExerciseDifficulty): List<Exercise> {
        return exerciseRepository.findByDifficulty(difficulty)
    }
}