package com.octopus.domain.repository

import com.octopus.domain.User
import java.util.*

interface IUserRepository {
    fun create(user: User): User
    fun findAll(): List<User>
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
}