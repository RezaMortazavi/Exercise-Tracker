package com.octopus.web

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.octopus.domain.*
import com.octopus.web.controller.ExerciseController
import com.octopus.web.controller.UserController

fun SchemaBuilder.userSchema(userController: UserController) {

    inputType<UserInput>{
        description = "The input of the user without the identifier"
    }

    type<User>{
        description = "User object"
    }

    query("users") {
        userController.getAll(this)
    }

    query("userById") {
        userController.getUserById(this)
    }

    query("userByEmail") {
        userController.getUserByEmail(this)
    }

    mutation("createUser") {
        userController.register(this)
    }
}

fun SchemaBuilder.exerciseSchema(exerciseController: ExerciseController) {

    inputType<ExerciseInput>{
        description = "The input of the exercise without the identifier"
    }

    type<Exercise>{
        description = "Exercise object"
    }

    enum<ExerciseType>()
    enum<ExerciseDifficulty>()

    query("exercises") {
        exerciseController.getAll(this)
    }

    query("exerciseById") {
        exerciseController.getExerciseById(this)
    }

    query("exerciseByType") {
        exerciseController.getAllByType(this)
    }

    query("exerciseByDifficulty") {
        exerciseController.getAllByDifficulty(this)
    }

    mutation("createExercise") {
        exerciseController.create(this)
    }
}