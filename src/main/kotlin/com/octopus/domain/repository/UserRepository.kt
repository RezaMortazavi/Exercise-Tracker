package com.octopus.domain.repository

import com.octopus.domain.User
import com.octopus.domain.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

interface IUserRepository {
    fun create(user: User): User
    fun findAll(): List<User>
    fun findById(id: UUID): User?
    fun findByEmail(email: String): User?
}

class UserRepository : IUserRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    override fun create(user: User): User {
        return transaction {
            user.copy(
                id = Users.insertAndGetId { row ->
                    row[email] = user.email
                    row[firstName] = user.firstName
                    row[lastName] = user.lastName
                }.value
            )
        }
    }

    override fun findAll(): List<User> {
        return transaction {
            Users
                .selectAll()
                .map { Users.toUser(it) }
        }
    }

    override fun findById(id: UUID): User? {
        return transaction {
            Users
                .select { Users.id eq id }
                .map { Users.toUser(it) }
                .firstOrNull()
        }
    }

    override fun findByEmail(email: String): User? {
        return transaction {
            Users
                .select { Users.email eq email }
                .map { Users.toUser(it) }
                .firstOrNull()
        }
    }
}