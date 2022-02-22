package com.octopus.web.controller

import com.apurebase.kgraphql.schema.dsl.operations.MutationDSL
import com.apurebase.kgraphql.schema.dsl.operations.QueryDSL
import com.octopus.domain.User
import com.octopus.domain.UserInput
import com.octopus.service.UserService

class UserController(private val userService: UserService) {

    fun register(dsl: MutationDSL) {
        dsl.description = "Create a new user"
        dsl.resolver { userInput: UserInput ->
            val user = User(email = userInput.email, firstName = userInput.firstName, lastName = userInput.lastName)
            userService.create(user)
        }
    }

    fun getAll(dsl: QueryDSL) {
        dsl.description = "get all users"
        dsl.resolver { ->
            userService.getAll()
        }
    }

    fun getUserById(dsl: QueryDSL) {
        dsl.description = "get a new user by id"
        dsl.resolver { uid: Long ->
            userService.getById(uid)
        }
    }

    fun getUserByEmail(dsl: QueryDSL) {
        dsl.description = "get a user by email"

        dsl.resolver { email: String ->
            userService.getByEmail(email)
        }
    }
}