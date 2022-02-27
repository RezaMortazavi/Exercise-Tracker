package com.octopus.domain

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

object Users : UUIDTable() {
    val email = varchar("email", 50)
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
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
    val id: UUID? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
)

data class UserInput(
    val email: String,
    val firstName: String,
    val lastName: String,
    val notifiers: NotifierInput? = null
)
