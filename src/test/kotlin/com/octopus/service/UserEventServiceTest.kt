package com.octopus.service

import com.octopus.config.DatabaseFactory
import com.octopus.config.ModuleConfig
import com.octopus.domain.Exercise
import com.octopus.domain.ExerciseDifficulty
import com.octopus.domain.ExerciseType
import com.octopus.domain.UserEvent
import com.octopus.domain.repository.IUserEventRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class UserEventServiceTest : BaseTest() {


    private val userEventService by inject<UserEventService>()

    @Test
    fun `event should not complete if progress not reached the end nor is complete already`() = runBlocking {
        // given
        val eventId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val exerciseId = 1L
        val userEvent = UserEvent(eventId, userId, 1, LocalDateTime.now(), LocalDateTime.now(), 20, false)

        val slot = slot<UserEvent>()
        declareMock<IUserEventRepository> {
            coEvery { findEventById(eventId) } returns userEvent
            coEvery { update(capture(slot)) } returns userEvent.copy(progress = 100)
        }

        declareMock<ExerciseService> {
            coEvery { getById(exerciseId) } returns Exercise(exerciseId, "Push Up", "Perfect for shoulder", null, null, 200, ExerciseType.STRENGTH, ExerciseDifficulty.MODERATE)
        }

        val userStatusServiceMock = declareMock<UserStatusService> {
            coEvery { recordActivity(userId) } returns Unit
        }

        // when
        val updateEvent = userEventService.updateEvent(eventId, 100)

        // then
        assertEquals(100, updateEvent.progress)
        assertEquals(100, slot.captured.progress)
        assertFalse { slot.captured.isCompleted }
        coVerify (exactly = 1) { userStatusServiceMock.recordActivity(userId) }
        confirmVerified(userStatusServiceMock)
    }

    @Test
    fun `event should remain completed if it's already completed`() = runBlocking {
        // given
        val eventId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val exerciseId = 1L
        val userEvent = UserEvent(eventId, userId, 1, LocalDateTime.now(), LocalDateTime.now(), 20, true)

        val slot = slot<UserEvent>()
        declareMock<IUserEventRepository> {
            coEvery { findEventById(eventId) } returns userEvent
            coEvery { update(capture(slot)) } returns userEvent.copy(progress = 100)
        }

        declareMock<ExerciseService> {
            coEvery { getById(exerciseId) } returns Exercise(exerciseId, "Push Up", "Perfect for shoulder", null, null, 200, ExerciseType.STRENGTH, ExerciseDifficulty.MODERATE)
        }

        val userStatusServiceMock = declareMock<UserStatusService> {
            coEvery { recordActivity(userId) } returns Unit
        }

        // when
        val updateEvent = userEventService.updateEvent(eventId, 100)

        // then
        assertEquals(100, updateEvent.progress)
        assertEquals(100, slot.captured.progress)
        assertTrue { slot.captured.isCompleted }
        coVerify(exactly = 1) { userStatusServiceMock.recordActivity(userId) }
        confirmVerified(userStatusServiceMock)
    }

    @Test
    fun `event should complete if progress reach the end`() = runBlocking {
        // given
        val eventId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val exerciseId = 1L
        val userEvent = UserEvent(eventId, userId, 1, LocalDateTime.now(), LocalDateTime.now(), 20, false)

        val slot = slot<UserEvent>()
        declareMock<IUserEventRepository> {
            coEvery { findEventById(eventId) } returns userEvent
            coEvery { update(capture(slot)) } returns userEvent.copy(progress = 200)
        }

        declareMock<ExerciseService> {
            coEvery { getById(exerciseId) } returns Exercise(exerciseId, "Push Up", "Perfect for shoulder", null, null, 200, ExerciseType.STRENGTH, ExerciseDifficulty.MODERATE)
        }

        val userStatusServiceMock = declareMock<UserStatusService> {
            coEvery { recordActivity(userId) } returns Unit
        }

        // when
        val updateEvent = userEventService.updateEvent(eventId, 200)

        // then
        assertEquals(200, updateEvent.progress)
        assertEquals(200, slot.captured.progress)
        assertTrue { slot.captured.isCompleted }
        coVerify(exactly = 1) { userStatusServiceMock.recordActivity(userId) }
        confirmVerified(userStatusServiceMock)
    }
}