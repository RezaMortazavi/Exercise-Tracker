package com.octopus.domain.repository

import com.octopus.domain.Exercise
import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType


interface IExerciseRepository {
    fun create(exercise: Exercise): Exercise
    fun findById(id: Long): Exercise?
    fun findAll(): List<Exercise>
    fun findByType(type: ExerciseType): List<Exercise>
    fun findByDifficulty(difficulty: ExerciseDifficulty): List<Exercise>
}