package com.octopus.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

object Notifiers : LongIdTable() {
    val userId = uuid("user_id").references(Users.id).index()
    val weekDay = integer("week_day")
    val hour = integer("hour")

    init {
        index(isUnique = false, weekDay, hour)
    }
}


fun Notifiers.toNotifier(row: ResultRow): Notifier {
    return Notifier(
        id = row[id].value,
        userId = row[userId],
        weekDay = row[weekDay],
        hour = row[hour]
    )
}

data class Notifier (
    val id: Long,
    val userId: UUID,
    val weekDay: Int,
    val hour: Int
)

data class NewNotifier (
    val userId: UUID? = null,
    val weekDay: Int,
    val hour: Int
)