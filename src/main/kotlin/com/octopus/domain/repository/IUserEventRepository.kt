package com.octopus.domain.repository

import com.octopus.domain.UserEvent
import java.util.UUID

interface IUserEventRepository {
    fun create(userId: UUID, exerciseId: Long): UserEvent
    fun update(userEvent: UserEvent): UserEvent?
    fun findEventById(eventId: UUID): UserEvent?
    fun findUserEvents(userId: UUID): List<UserEvent>
}