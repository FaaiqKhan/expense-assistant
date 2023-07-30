package com.practice.expenseAssistant.utils

import java.time.LocalDate

object Utils {

    val daysMap = mapOf(
        "SUNDAY" to 0,
        "MONDAY" to 1,
        "TUESDAY" to 2,
        "WEDNESDAY" to 3,
        "THURSDAY" to 4,
        "FRIDAY" to 5,
        "SATURDAY" to 6
    )

    fun createCalenderDays(localDate: LocalDate): List<Int> {
        val daysToAdvance = daysMap.getValue(localDate.withDayOfMonth(1).dayOfWeek.name)

        val previousMonth = localDate.minusMonths(1)
        val datesToAdvance = previousMonth.month.maxLength() - daysToAdvance
        // 42 is total number of grids shown in grid view
        val nextMonthDays = 42 - daysToAdvance - localDate.month.maxLength()

        val numberOfDaysInPreviousMonth = List(daysToAdvance) { datesToAdvance + it + 1 }
        val numberOfDaysInCurrentMonth = List(localDate.month.maxLength()) { it + 1 }
        val numberOfDaysInNextMonth = List(nextMonthDays) { it + 1 }

        return numberOfDaysInPreviousMonth + numberOfDaysInCurrentMonth + numberOfDaysInNextMonth
    }
}