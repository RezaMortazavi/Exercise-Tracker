package com.octopus.service

import com.octopus.domain.User
import com.octopus.domain.repository.IUserRepository
import com.octopus.exception.NotFoundException

class UserService(private val userRepository: IUserRepository) {
    fun create(user: User): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Email already registered!" }
        }

        return userRepository.create(user)
    }

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun getById(id: Long): User {
        return userRepository.findById(id) ?: throw NotFoundException("User not found with id: $id")
    }

    fun getByEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw NotFoundException("User not found with email: $email")
    }
}