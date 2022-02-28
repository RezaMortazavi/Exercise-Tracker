package com.octopus.service

import com.octopus.domain.NewNotifier
import com.octopus.domain.NewUser
import com.octopus.domain.User
import com.octopus.domain.repository.IUserRepository
import com.octopus.exception.NotFoundException
import org.slf4j.LoggerFactory
import java.util.*

class UserService(private val userRepository: IUserRepository,
                  private val notifierService: NotifierService,
                  private val userStatusService: UserStatusService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    suspend fun create(user: NewUser, notifiers: List<NewNotifier>?): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Email already registered!" }
        }

        return userRepository.create(user).also { createdUser ->
            log.debug("[create] User created: $createdUser")
            // create notifiers for user if requested
            if (notifiers != null) {
                createNotifier(createdUser.id, notifiers)
            }

            // init user status for new users
            initUserStatus(createdUser.id)
        }
    }

    suspend fun getAll(): List<User> = userRepository.findAll()

    suspend fun getById(id: UUID): User {
        return userRepository.findById(id) ?: throw NotFoundException("User not found with id: $id")
    }

    suspend fun getByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw NotFoundException("User not found with email: $email")
    }

    private suspend fun createNotifier(userId: UUID, notifiers: List<NewNotifier>) {
        val userNotifiers = notifiers.map { NewNotifier(userId, it.weekDay, it.hour) }
        notifierService.createNotifiersForUer(userNotifiers)
    }

    private suspend fun initUserStatus(userId: UUID) = userStatusService.initUserStatus(userId)
}