package com.octopus.helper

import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Custom implementation simulating a cron job
 */
abstract class Scheduler {
    private val log = LoggerFactory.getLogger(this::class.java)

    protected abstract val timer: Long
    protected abstract fun isOnTime(): Boolean

    suspend fun schedule(message: suspend () -> Unit) {
        while (true) {
            delay(timer)
            if (isOnTime()) {
                log.debug("Dispatching")
                message()
            }
        }
    }

    companion object {
        fun create(schedulerType: SchedulerType): Scheduler {
            return when(schedulerType) {
                SchedulerType.HOURLY -> HourlyScheduler
            }
        }
    }
}

/**
 * Schedule a task to run in every hour:00
 * example: 01:00, 02:00, etc
 */
object HourlyScheduler : Scheduler() {
    override val timer: Long = 60 * 1000

    override fun isOnTime(): Boolean {
        return getCurrentMinute() == 0
    }

    private fun getCurrentMinute() = Calendar.getInstance().get(Calendar.MINUTE)
}

enum class SchedulerType { HOURLY }