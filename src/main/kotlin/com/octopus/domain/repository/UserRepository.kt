package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory.dbQuery
import com.octopus.domain.NewUser
import com.octopus.domain.User
import com.octopus.domain.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.util.*

interface IUserRepository {
    suspend fun create(user: NewUser): User
    suspend fun findAll(): List<User>
    suspend fun findById(id: UUID): User?
    suspend fun findByEmail(email: String): User?
}

class UserRepository : IUserRepository {

    override suspend fun create(user: NewUser): User {
        val id = dbQuery {
            Users.insertAndGetId { row ->
                row[email] = user.email
                row[firstName] = user.firstName
                row[lastName] = user.lastName
            }.value
        }

        return findById(id)!!
    }

    override suspend fun findAll(): List<User> = dbQuery {
        Users
            .selectAll()
            .map { toUser(it) }

    }

    override suspend fun findById(id: UUID): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map { toUser(it) }
            .firstOrNull()

    }

    override suspend fun findByEmail(email: String): User? = dbQuery {
        Users
            .select { Users.email eq email }
            .map { toUser(it) }
            .firstOrNull()

    }


    private fun toUser(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            email = row[Users.email],
            firstName = row[Users.firstName],
            lastName = row[Users.lastName],
        )
    }
}