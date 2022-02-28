package com.octopus.service

import com.octopus.domain.User
import com.octopus.domain.repository.INotifierRepository
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.koin.core.component.inject
import org.koin.test.mock.declareMock
import java.util.*

class NotifierServiceTest : BaseTest() {
    private val notifierService by inject<NotifierService>()

    @Test
    fun `should notify users if there are any registered`() = runBlocking {
        // given
        val testUsers = mutableListOf(User(UUID.randomUUID(), "r.mortazavi86@gmail.com", "reza", "mortazavi"))
        declareMock<INotifierRepository> {
            coEvery { findRegisteredUsersNotActiveToday(any(), any()) } returns testUsers
        }

        val notificationServiceMock = declareMock<NotificationService> {
            coEvery { sendNotification(testUsers) } returns Unit
        }

        // when
        notifierService.notifyEligibleUsers()

        // then
        coVerify(exactly = 1) { notificationServiceMock.sendNotification(testUsers) }
    }
}