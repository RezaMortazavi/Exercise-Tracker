package com.octopus.service

import com.octopus.config.ModuleConfig
import io.mockk.mockkClass
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule

open class BaseTest: KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(ModuleConfig.modules())
    }

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz.java.kotlin)
    }
}