package com.practice.expenseAssistant.utils

import com.practice.expenseAssistant.data.CalendarDateModel
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

    fun createCalenderDays(localDate: LocalDate, todayDate: LocalDate): List<CalendarDateModel> {
        val daysToAdvance = daysMap.getValue(localDate.withDayOfMonth(1).dayOfWeek.name)

        val previousMonth = localDate.minusMonths(1)
        val nextMonth = localDate.plusMonths(1)
        val startDateOfPreviousMonth = previousMonth.withDayOfMonth(
            previousMonth.month.maxLength() - daysToAdvance
        )
        // 42 is total number of grids shown in grid view
        val nextMonthDays = 42 - daysToAdvance - localDate.month.maxLength()
        var index = -1
        val numberOfDaysInPreviousMonth = List(daysToAdvance) {
            index++
            CalendarDateModel(
                id = index,
                date = startDateOfPreviousMonth.plusDays((it + 1).toLong()),
                isSelected = false,
                isCurrentMonthDate = previousMonth.month == localDate.month
            )
        }
        val numberOfDaysInCurrentMonth = List(localDate.month.maxLength()) {
            index++
            val calculatedDate = localDate.plusDays(it.toLong())
            CalendarDateModel(
                id = index,
                date = calculatedDate,
                isSelected = calculatedDate.dayOfMonth == todayDate.dayOfMonth,
                isCurrentMonthDate = LocalDate.now().month == localDate.month
            )
        }
        val numberOfDaysInNextMonth = List(nextMonthDays) {
            index++
            CalendarDateModel(
                id = index,
                date = nextMonth.plusDays(it.toLong()),
                isSelected = false,
                isCurrentMonthDate = nextMonth.month == localDate.month
            )
        }

        return numberOfDaysInPreviousMonth + numberOfDaysInCurrentMonth + numberOfDaysInNextMonth
    }
}