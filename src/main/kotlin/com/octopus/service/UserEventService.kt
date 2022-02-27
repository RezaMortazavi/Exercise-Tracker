package com.octopus.service

import com.octopus.domain.UserEvent
import com.octopus.domain.repository.IExerciseRepository
import com.octopus.domain.repository.IUserEventRepository
import com.octopus.domain.repository.IUserRepository
import com.octopus.exception.NotFoundException
import org.slf4j.LoggerFactory
import java.util.*

class UserEventService(private val userEventRepository: IUserEventRepository,
                       private val userRepository: IUserRepository,
                       private val exerciseRepository: IExerciseRepository,
                       private val userStatusService: UserStatusService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun generateUserEvent(userId: UUID, exerciseId: Long): UserEvent {
        log.debug("[generateUserEvent] userId: $userId, exerciseId: $exerciseId")

        // throws exception if user nor exercise found
        userRepository.findById(userId) ?: throw NotFoundException("User not found")
        val exercise = exerciseRepository.findById(exerciseId) ?: throw NotFoundException("Exercise not found")

        return userEventRepository.create(userId, exercise).also {
            log.debug("[generateUserEvent] generatedEvent: $it")
            hitUserActivity(userId)
        }
    }

    fun updateEvent(eventId: UUID, progress: Long): UserEvent {
        log.debug("[updateEvent] eventId: $eventId, progress: $progress")

        val userEvent = userEventRepository.findEventById(eventId) ?: throw NotFoundException("Event not found")

        val isCompleted = userEvent.isCompleted || progress >= userEvent.exercise.duration

        return userEventRepository.update(userEvent.copy(progress = progress, isCompleted = isCompleted))!!.also {
            log.debug("[updateEvent] Updated Event: $it")
            hitUserActivity(it.userId)
        }

    }

    fun getUserEvents(userId: UUID): List<UserEvent> {
        log.debug("[getUserEvents] userId: $userId")

        // throws exception if user not found
        userRepository.findById(userId) ?: throw NotFoundException("User not found")

        return userEventRepository.findUserEvents(userId).also {
            log.debug("[getUserEvents] found events: $it")
        }
    }

    private fun hitUserActivity(userId: UUID) = userStatusService.recordActivity(userId)
}