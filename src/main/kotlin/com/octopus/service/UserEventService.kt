package com.octopus.service

import com.octopus.domain.UserEvent
import com.octopus.domain.repository.IUserEventRepository
import org.slf4j.LoggerFactory
import java.util.UUID

class UserEventService(private val repository: IUserEventRepository,
                       private val userService: UserService,
                       private val exerciseService: ExerciseService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun generateUserEvent(userId: UUID, exerciseId: Long): UserEvent {
        log.debug("[generateUserEvent] userId: $userId, exerciseId: $exerciseId")

        // throws exception if user nor exercise found
        userService.getById(userId)
        exerciseService.getById(exerciseId)

        return repository.create(userId, exerciseId).also {
            log.debug("[generateUserEvent] generatedEvent: $it")
        }
    }

    fun getUserEvents(userId: UUID): List<UserEvent> {
        log.debug("[getUserEvents] userId: $userId")

        // throws exception if user nor exercise found
        userService.getById(userId)

        return repository.findUserEvents(userId).also {
            log.debug("[getUserEvents] found events: $it")
        }
    }
}