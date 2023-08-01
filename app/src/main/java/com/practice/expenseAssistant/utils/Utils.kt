package com.practice.expenseAssistant.utils

import com.practice.expenseAssistant.data.ExpenseModel
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

    fun createCalenderDays(localDate: LocalDate): List<ExpenseModel> {
        val daysToAdvance = daysMap.getValue(localDate.withDayOfMonth(1).dayOfWeek.name)

        val previousMonth = localDate.minusMonths(1)
        val nextMonth = localDate.plusMonths(1)
        val startDateOfPreviousMonth = previousMonth.withDayOfMonth(
            previousMonth.month.maxLength() - daysToAdvance
        )
        // 42 is total number of grids shown in grid view
        val nextMonthDays = 42 - daysToAdvance - localDate.month.maxLength()

        val numberOfDaysInPreviousMonth = List(daysToAdvance) {
            ExpenseModel(
                date = startDateOfPreviousMonth.plusDays((it + 1).toLong()),
                isSelected = false,
                isCurrentMonthDate = false
            )
        }
        val numberOfDaysInCurrentMonth = List(localDate.month.maxLength()) {
            ExpenseModel(
                date = localDate.plusDays(it.toLong()),
                expense = ((it + 1) * 20).toString(),
                isSelected = LocalDate.now().dayOfMonth == it + 1,
                isCurrentMonthDate = true
            )
        }
        val numberOfDaysInNextMonth = List(nextMonthDays) {
            ExpenseModel(
                date = nextMonth.plusDays(it.toLong()),
                isSelected = false,
                isCurrentMonthDate = false
            )
        }

        return numberOfDaysInPreviousMonth + numberOfDaysInCurrentMonth + numberOfDaysInNextMonth
    }
}