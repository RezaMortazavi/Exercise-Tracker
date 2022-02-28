package com.octopus.service

import com.octopus.domain.UserEvent
import com.octopus.domain.repository.IUserEventRepository
import com.octopus.exception.NotFoundException
import org.slf4j.LoggerFactory
import java.util.*

class UserEventService(private val userEventRepository: IUserEventRepository,
                       private val userService: UserService,
                       private val exerciseService: ExerciseService,
                       private val userStatusService: UserStatusService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Start an event for a user
     *
     * Updates user's last active time
     */
    suspend fun generateUserEvent(userId: UUID, exerciseId: Long): UserEvent {
        log.debug("[generateUserEvent] userId: $userId, exerciseId: $exerciseId")

        // throws exception if user nor exercise found
        userService.getById(userId)
        exerciseService.getById(exerciseId)

        return userEventRepository.create(userId, exerciseId).also {
            log.debug("[generateUserEvent] generatedEvent: $it")
            hitUserActivity(userId)
        }
    }

    /**
     * Update event progress
     * Marked as completed if user reached the end of the exerciseDuration (progress is equal to event duration)
     * Once marked as completed it will remain as it is and will not change by progress update
     *
     * Updates user's last active time
     */
    suspend fun updateEvent(eventId: UUID, progress: Long): UserEvent {
        log.debug("[updateEvent] eventId: $eventId, progress: $progress")

        val userEvent = userEventRepository.findEventById(eventId) ?: throw NotFoundException("Event not found")

        val isCompleted = isEventCompleted(userEvent, progress)

        return userEventRepository.update(userEvent.copy(progress = progress, isCompleted = isCompleted))!!.also {
            log.debug("[updateEvent] Updated Event: $it")
            hitUserActivity(it.userId)
        }
    }

    suspend fun getUserEvents(userId: UUID): List<UserEvent> {
        log.debug("[getUserEvents] userId: $userId")

        // throws exception if user not found
        userService.getById(userId)

        return userEventRepository.findUserEvents(userId).also {
            log.debug("[getUserEvents] found events: $it")
        }
    }

    private suspend fun isEventCompleted(userEvent: UserEvent, progress: Long): Boolean {
        var isCompleted = true
        if (!userEvent.isCompleted) {
            val exercise = exerciseService.getById(userEvent.exerciseId)
            isCompleted = hasProgressReachedEnd(exercise.duration, progress)
        }
        return isCompleted
    }

    private fun hasProgressReachedEnd(exerciseDuration: Long, progress: Long) = progress >= exerciseDuration

    private suspend fun hitUserActivity(userId: UUID) = userStatusService.recordActivity(userId)
}