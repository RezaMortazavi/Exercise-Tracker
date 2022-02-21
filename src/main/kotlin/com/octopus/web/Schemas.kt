package com.octopus.web

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import com.octopus.domain.User
import com.octopus.domain.UserInput
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