package com.octopus.web.model

import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType

data class ExerciseInput(
    val title: String,
    val description: String,
    val duration: Long,
    val type: ExerciseType,
    val difficulty: ExerciseDifficulty
)