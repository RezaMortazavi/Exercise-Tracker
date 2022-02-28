package com.octopus.web.model

data class NotifierInput(
    val weekDay: List<Int>,
    val hour: Int
) {
    fun validate(): Boolean {
        val validWeekDay = weekDay.all {
            it in 1..7
        }

        val validHour = hour in 0 .. 23

        return validWeekDay && validHour
    }
}

