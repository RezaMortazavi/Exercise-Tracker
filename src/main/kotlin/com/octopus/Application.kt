package com.octopus

import com.apurebase.kgraphql.GraphQL
import com.octopus.config.DatabaseFactory
import com.octopus.config.ModuleConfig
import com.octopus.web.controller.UserController
import com.octopus.web.userSchema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val userController: UserController by inject()

    DatabaseFactory.connect()

    install(Koin) {
        modules(ModuleConfig.userModule)
    }

    install(CallLogging)
    install(GraphQL) {
        playground = true
        schema {
            userSchema(userController)
        }
    }
}
