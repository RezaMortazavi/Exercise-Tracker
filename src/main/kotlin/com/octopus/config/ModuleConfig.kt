package com.octopus.config

import com.octopus.domain.repository.*
import com.octopus.service.*
import com.octopus.web.controller.ExerciseController
import com.octopus.web.controller.UserController
import com.octopus.web.controller.UserEventController
import org.koin.dsl.module

/**
 * DI Module Container
 */
object ModuleConfig {
    private val userModule = module {
        single { UserController(get()) } // get() Will resolve UserService
        single { UserService(get(), get(), get()) } // get() Will resolve IUserRepository, NotifierService, UserStatusService
        single { NotifierService(get(), get()) } // get() Will resolve INotifierRepository, NotificationService
        single<IUserRepository> { UserRepository() }
        single<INotifierRepository> { NotifierRepository() }
    }

    private val exerciseModule = module {
        single { ExerciseController(get()) } // get() Will resolve UserService
        single { ExerciseService(get()) } // get() Will resolve IExerciseRepository
        single<IExerciseRepository> { ExerciseRepository() }
    }

    private val userEventModule = module {
        single { UserEventController(get()) } // get() Will resolve UserService
        single { UserEventService(get(), get(), get(), get()) } // get() Will resolve IUserEventRepository, IUserRepository, IExerciseRepository, UserStatusService
        single<IUserEventRepository> { UserEventRepository() }
    }

    private val userStatusModule = module {
        single { UserStatusService(get()) }
        single<IUserStatusRepository> { UserStatusRepository() }
    }

    private val notificationModule = module {
        single { NotificationService(get()) }
        single<INotificationRepository> { NotificationRepository() }
    }

    fun modules() = mutableListOf(userModule, exerciseModule, userEventModule, userStatusModule, notificationModule)
}