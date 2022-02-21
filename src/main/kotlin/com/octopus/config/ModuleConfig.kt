package com.octopus.config

import com.octopus.domain.repository.IUserRepository
import com.octopus.domain.repository.UserRepository
import com.octopus.service.UserService
import com.octopus.web.controller.UserController
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
}