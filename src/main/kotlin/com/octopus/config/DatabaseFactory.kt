package com.octopus.config

import com.octopus.domain.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory


object DatabaseFactory {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun connect() {
        log.info("Initialising database")
        val pool = hikari()
        Database.connect(pool)
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    fun createTables() {
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Exercises)
            SchemaUtils.create(UserEvents)
            SchemaUtils.create(UserStatuses)
            SchemaUtils.create(Notifiers)
            SchemaUtils.create(Notifications)
        }
    }

    suspend fun <T> dbQuery(
        block: suspend () -> T
    ): T =
        newSuspendedTransaction { block() }
}