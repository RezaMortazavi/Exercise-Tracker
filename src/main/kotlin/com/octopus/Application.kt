package com.octopus

import com.apurebase.kgraphql.GraphQL
import com.octopus.config.DatabaseFactory
import com.octopus.config.ModuleConfig
import com.octopus.helper.Scheduler
import com.octopus.helper.SchedulerType
import com.octopus.service.NotifierService
import com.octopus.web.addGenericScalars
import com.octopus.web.controller.ExerciseController
import com.octopus.web.controller.UserController
import com.octopus.web.controller.UserEventController
import com.octopus.web.exerciseSchema
import com.octopus.web.userEventsSchema
import com.octopus.web.userSchema
import io.ktor.application.*
import io.ktor.server.netty.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject


fun main(args: Array<String>): Unit = EngineMain.main(args)

@ExperimentalCoroutinesApi
fun Application.module() {
    val userController: UserController by inject()
    val exerciseController: ExerciseController by inject()
    val userEventController: UserEventController by inject()

    DatabaseFactory.connect()
    DatabaseFactory.createTables() // for testing

    install(Koin) {
        modules(ModuleConfig.modules())
    }

    install(GraphQL) {
        playground = true // for testing
        wrapErrors = true // for testing
        schema {
            addGenericScalars()
            userSchema(userController)
            exerciseSchema(exerciseController)
            userEventsSchema(userEventController)
        }
    }

    runNotifierJob()
}

fun Application.runNotifierJob() {
    val notifierService: NotifierService by inject()
    val scheduler = Scheduler.create(SchedulerType.HOURLY)
    launch {
        scheduler.schedule {
            notifierService.notifyEligibleUsers()
        }
    }
}
