package com.octopus.web.controller

import com.apurebase.kgraphql.schema.dsl.operations.MutationDSL
import com.apurebase.kgraphql.schema.dsl.operations.QueryDSL
import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType
import com.octopus.domain.NewExercise
import com.octopus.service.ExerciseService
import com.octopus.web.model.ExerciseInput
import org.slf4j.LoggerFactory

class ExerciseController(private val exerciseService: ExerciseService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(dsl: MutationDSL) {
        dsl.description = "Create a new exercise"
        dsl.resolver { exerciseInput: ExerciseInput ->
            log.debug("[create] exerciseInput: $exerciseInput")

            val exercise = NewExercise(title = exerciseInput.title, description = exerciseInput.description,
                duration = exerciseInput.duration, type = exerciseInput.type, difficulty = exerciseInput.difficulty)
            exerciseService.create(exercise)
        }
    }

    fun getAll(dsl: QueryDSL) {
        dsl.description = "get all exercises"
        dsl.resolver { ->
            log.debug("[getAll]")

            exerciseService.getAll()
        }
    }

    fun getExerciseById(dsl: QueryDSL) {
        dsl.description = "get a exercise by id"
        dsl.resolver { uid: Long ->
            log.debug("[getExerciseById] uid: $uid")

            exerciseService.getById(uid)
        }
    }

    fun getAllByType(dsl: QueryDSL) {
        dsl.description = "get all exercises by type"
        dsl.resolver { type: ExerciseType ->
            log.debug("[getAllByType] type: $type")

            exerciseService.getAllByType(type)
        }
    }

    fun getAllByDifficulty(dsl: QueryDSL) {
        dsl.description = "get all exercises by difficulty"
        dsl.resolver { difficulty: ExerciseDifficulty ->
            log.debug("[getAllByDifficulty] difficulty: $difficulty")

            exerciseService.getAllByDifficulty(difficulty)
        }
    }
}