package com.octopus.web.controller

import com.apurebase.kgraphql.schema.dsl.operations.MutationDSL
import com.apurebase.kgraphql.schema.dsl.operations.QueryDSL
import com.octopus.domain.NewNotifier
import com.octopus.domain.NewUser
import com.octopus.service.UserService
import com.octopus.web.model.UserInput
import io.ktor.features.*
import java.util.*

class UserController(private val userService: UserService) {

    fun register(dsl: MutationDSL) {
        dsl.description = "Create a new user"
        dsl.resolver { userInput: UserInput ->
            val user = NewUser(email = userInput.email, firstName = userInput.firstName, lastName = userInput.lastName)
            val notifiers = userInput.notifiers?.let { notifierInput ->
                if (!notifierInput.validate()) throw BadRequestException("Week day or hour is not valid")
                notifierInput.weekDay.map { weekDay ->
                    NewNotifier(weekDay = weekDay, hour = notifierInput.hour)
                }
            }
            userService.create(user, notifiers)
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
        dsl.resolver { uid: UUID ->
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