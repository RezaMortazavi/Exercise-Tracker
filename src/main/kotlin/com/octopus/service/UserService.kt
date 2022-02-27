package com.octopus.service

import com.octopus.domain.Notifier
import com.octopus.domain.User
import com.octopus.domain.repository.IUserRepository
import com.octopus.exception.NotFoundException
import org.slf4j.LoggerFactory
import java.util.*

class UserService(private val userRepository: IUserRepository,
                  private val notifierService: NotifierService,
                  private val userStatusService: UserStatusService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(user: User, notifiers: List<Notifier>?): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Email already registered!" }
        }

        return userRepository.create(user).also { createdUser ->
            log.debug("[create] User created: $createdUser")
            require(createdUser.id != null) { "Unable to create user" }
            // create notifiers for user if requested
            if (notifiers != null) {
                createNotifier(createdUser.id, notifiers)
            }

            // init user status for new users
            initUserStatus(createdUser.id)
        }
    }

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun getById(id: UUID): User {
        return userRepository.findById(id) ?: throw NotFoundException("User not found with id: $id")
    }

    fun getByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw NotFoundException("User not found with email: $email")
    }

    private fun createNotifier(userId: UUID, notifiers: List<Notifier>) {
        val userNotifiers = notifiers.map { it.copy(userId = userId) }
        notifierService.createNotifiersForUer(userNotifiers)
    }

    private fun initUserStatus(userId: UUID) = userStatusService.initUserStatus(userId)
}