package com.octopus.service

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.core.component.inject

class UserServiceTest : BaseTest() {
    private val userService by inject<UserService>()

    @Test
    fun `create a `() = runBlocking {
        // given

        // when
//        userService.create()
    }
}