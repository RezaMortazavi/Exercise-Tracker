package com.octopus.domain

import io.ktor.util.date.*
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*
import kotlin.collections.List

object Notifiers : LongIdTable() {
    val userId = uuid("user_id").references(Users.id).index()
    val weekDay = integer("week_day")
    val hour = integer("hour")

    init {
        index(isUnique = false, weekDay, hour)
    }

    fun toNotifier(row: ResultRow): Notifier {
        WeekDay
        return Notifier(
            id = row[id].value,
            userId = row[userId],
            weekDay = row[weekDay],
            hour = row[hour]
        )
    }
}

data class Notifier (
    val id: Long? = null,
    val userId: UUID? = null,
    val weekDay: Int,
    val hour: Int,
)

data class NotifierInput(
    val weekDay: List<Int>,
    val hour: Int
)