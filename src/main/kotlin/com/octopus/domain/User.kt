package com.octopus.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users : LongIdTable() {
    val email = varchar("email", 255)
    val firstName = varchar("firstName", 255)
    val lastName = varchar("lastName", 255)
    // we could have added other properties like gender, wight, height, birthday, etc

    fun toUser(row: ResultRow): User {
        return User(
            id = row[id].value,
            email = row[email],
            firstName = row[firstName],
            lastName = row[lastName],
        )
    }
}

data class User(
    val id: Long? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
)

data class UserInput(
    val email: String,
    val firstName: String,
    val lastName: String,
)
