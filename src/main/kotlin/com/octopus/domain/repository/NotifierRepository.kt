package com.octopus.domain.repository

import com.octopus.domain.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

interface INotifierRepository {
    fun create(notifier: Notifier): Notifier
    fun batchCreate(notifiers: List<Notifier>)
    fun findByUserId(userId: UUID): List<Notifier>
    fun findRegisteredUsersNotActiveToday(weekDay: Int, hour: Int): List<User>
}

class NotifierRepository : INotifierRepository {
    init {
        transaction {
            SchemaUtils.create(Notifiers)
        }
    }

    override fun create(notifier: Notifier): Notifier {
        return transaction {
            notifier.copy(
                id = Notifiers.insertAndGetId { row ->
                    row[userId] = notifier.userId!!
                    row[weekDay] = notifier.weekDay
                    row[hour] = notifier.hour
                }.value
            )
        }
    }

    override fun batchCreate(notifiers: List<Notifier>) {
        transaction {
            Notifiers.batchInsert(notifiers) { item: Notifier ->
                this[Notifiers.userId] = item.userId!!
                this[Notifiers.weekDay] = item.weekDay
                this[Notifiers.hour] = item.hour
            }
        }
    }

    override fun findByUserId(userId: UUID): List<Notifier> {
        return transaction {
            Notifiers
                .select { Notifiers.userId eq userId }
                .map { Notifiers.toNotifier(it) }
        }
    }

    override fun findRegisteredUsersNotActiveToday(weekDay: Int, hour: Int): List<User> {
        val today = LocalDate.now().atStartOfDay()

        return transaction {
            (Notifiers innerJoin Users innerJoin UserStatuses)
                .select {
                            (Notifiers.weekDay eq weekDay) and
                            (Notifiers.hour eq hour) and
                            (UserStatuses.lastActivityAt less today)
                }
                .map { Users.toUser(it) }
        }
    }

}