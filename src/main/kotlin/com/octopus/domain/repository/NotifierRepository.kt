package com.octopus.domain.repository

import com.octopus.config.DatabaseFactory.dbQuery
import com.octopus.domain.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import java.time.LocalDate
import java.util.*

interface INotifierRepository {
    suspend fun create(notifier: NewNotifier): Notifier
    suspend fun batchCreate(notifiers: List<NewNotifier>)
    suspend fun findById(id: Long): Notifier?
    suspend fun findByUserId(userId: UUID): List<Notifier>
    suspend fun findRegisteredUsersNotActiveToday(weekDay: Int, hour: Int): List<User>
}

class NotifierRepository : INotifierRepository {

    override suspend fun create(notifier: NewNotifier): Notifier {
        val id = dbQuery {
            Notifiers.insertAndGetId { row ->
                    row[userId] = notifier.userId!!
                    row[weekDay] = notifier.weekDay
                    row[hour] = notifier.hour
                }.value
        }

        return findById(id)!!
    }

    override suspend fun batchCreate(notifiers: List<NewNotifier>) {
        dbQuery {
            Notifiers.batchInsert(notifiers) { item: NewNotifier ->
                this[Notifiers.userId] = item.userId!!
                this[Notifiers.weekDay] = item.weekDay
                this[Notifiers.hour] = item.hour
            }
        }
    }

    override suspend fun findById(id: Long): Notifier? = dbQuery {
        Notifiers
            .select { Notifiers.id eq id }
            .map { Notifiers.toNotifier(it) }
            .firstOrNull()

    }

    override suspend fun findByUserId(userId: UUID): List<Notifier> = dbQuery {
        Notifiers
            .select { Notifiers.userId eq userId }
            .map { Notifiers.toNotifier(it) }

    }

    override suspend fun findRegisteredUsersNotActiveToday(weekDay: Int, hour: Int): List<User> {
        val today = LocalDate.now().atStartOfDay()

        return dbQuery {
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