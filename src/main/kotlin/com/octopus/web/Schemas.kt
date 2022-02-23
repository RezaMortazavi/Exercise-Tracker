package com.octopus.web

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.octopus.domain.*
import com.octopus.web.controller.ExerciseController
import com.octopus.web.controller.UserController
import com.octopus.web.controller.UserEventController
import java.util.*

fun SchemaBuilder.addScalars() {
    stringScalar<UUID> {
        deserialize = { uuid: String -> UUID.fromString(uuid) }
        serialize = { uuid: UUID -> uuid.toString() }
    }
}

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

fun SchemaBuilder.userEventsSchema(userEventController: UserEventController) {

    inputType<UserEventInput>{
        description = "The input to generate an event for a user"
    }

    type<UserEvent>{
        description = "UserEvent object"
    }

    query("userEvents") {
        userEventController.getUserEvents(this)
    }

    mutation("generateEvent") {
        userEventController.generateUserEvent(this)
    }

    mutation("updateEvent") {
        userEventController.updateEvent(this)
    }
}