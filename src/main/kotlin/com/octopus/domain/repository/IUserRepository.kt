package com.octopus.domain.repository

import com.octopus.domain.User

interface IUserRepository {
    fun create(user: User): User
    fun findAll(): List<User>
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
}