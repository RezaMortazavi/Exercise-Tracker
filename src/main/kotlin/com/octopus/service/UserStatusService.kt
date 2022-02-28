package com.octopus.service

import com.octopus.domain.UserStatus
import com.octopus.domain.repository.IUserStatusRepository
import com.octopus.exception.NotFoundException
import org.slf4j.LoggerFactory
import java.util.*

class UserStatusService(private val repository: IUserStatusRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    suspend fun initUserStatus(userId: UUID) = repository.create(userId).also {
        log.info("[createUserStatus] UserStatus created: $it")
    }

    suspend fun recordActivity(userId: UUID) {
        getUserStatus(userId).apply {
            log.info("[recordActivity] userId: $userId")
            repository.update(this)
        }
    }

    private suspend fun getUserStatus(userId: UUID): UserStatus = repository.findByUserId(userId)
        ?: throw NotFoundException("User status not found, userId: $userId")
}