package com.octopus.web.controller

import com.apurebase.kgraphql.schema.dsl.operations.MutationDSL
import com.apurebase.kgraphql.schema.dsl.operations.QueryDSL
import com.octopus.service.UserEventService
import com.octopus.web.model.UpdateUserEventInput
import com.octopus.web.model.UserEventInput
import org.slf4j.LoggerFactory
import java.util.*

class UserEventController(private val service: UserEventService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun generateUserEvent(dsl: MutationDSL) {
        dsl.apply {
            description = "Generate UserEvent"
            resolver { eventInput: UserEventInput ->
                log.debug("[generateUserEvent] event: $eventInput")

                service.generateUserEvent(eventInput.userId, eventInput.exerciseId)
            }
        }
    }

    fun updateEvent(dsl: MutationDSL) {
        dsl.apply {
            description = "Update UserEvent"
            resolver { eventId: UUID, eventInput: UpdateUserEventInput ->
                log.debug("[updateEvent] event: $eventInput")

                service.updateEvent(eventId, eventInput.progress)
            }
        }
    }

    fun getUserEvents(dsl: QueryDSL) {
        dsl.apply {
            description = "get user events"
            resolver { userId: UUID ->
                log.debug("[getUserEvents] userId: $userId")

                service.getUserEvents(userId)
            }
        }
    }
}