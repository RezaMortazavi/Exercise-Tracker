package com.octopus.config

import com.octopus.domain.repository.*
import com.octopus.service.ExerciseService
import com.octopus.service.UserEventService
import com.octopus.service.UserService
import com.octopus.web.controller.ExerciseController
import com.octopus.web.controller.UserController
import com.octopus.web.controller.UserEventController
import org.koin.dsl.module

/**
 * DI Module Container
 */
object ModuleConfig {
    val userModule = module {
        single { UserController(get()) } // get() Will resolve UserService
        single { UserService(get()) } // get() Will resolve IUserRepository
        single<IUserRepository> { UserRepository() }
    }

    val exerciseModule = module {
        single { ExerciseController(get()) } // get() Will resolve UserService
        single { ExerciseService(get()) } // get() Will resolve IExerciseRepository
        single<IExerciseRepository> { ExerciseRepository() }
    }

    val userEventModule = module {
        single { UserEventController(get()) } // get() Will resolve UserService
        single { UserEventService(get(), get(), get()) } // get() Will resolve IUserEventRepository, UserService, ExerciseService
        single<IUserEventRepository> { UserEventRepository() }
    }
}