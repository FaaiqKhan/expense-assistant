package com.practice.expenseAssistant.utils

import com.practice.expenseAssistant.data.CalendarDateModel
import com.practice.expenseAssistant.data.TransactionModel
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

    fun createCalenderDays(
        month: LocalDate,
        todayDate: LocalDate,
        transactions: Map<LocalDate, List<TransactionModel>> = mapOf()
    ): List<CalendarDateModel> {
        val daysToAdvance = daysMap.getValue(month.withDayOfMonth(1).dayOfWeek.name)

        val previousMonth = month.minusMonths(1)
        val nextMonth = month.plusMonths(1)
        val startDateOfPreviousMonth = previousMonth.withDayOfMonth(
            previousMonth.month.maxLength() - daysToAdvance
        )
        // 42 is total number of grids shown in grid view
        val nextMonthDays = 42 - daysToAdvance - month.month.maxLength()
        var index = -1
        val numberOfDaysInPreviousMonth = List(daysToAdvance) {
            index++
            val calculatedDate = startDateOfPreviousMonth.plusDays((it + 1).toLong())
            val todayTransactions = transactions[calculatedDate]
            val totalExpense = todayTransactions?.sumOf { transaction ->
                if (transaction.categoryType == CategoryType.EXPENSE) transaction.amount else 0.0
            } ?: 0.0
            val totalIncome = todayTransactions?.sumOf { transaction ->
                if (transaction.categoryType == CategoryType.INCOME) transaction.amount else 0.0
            } ?: 0.0
            CalendarDateModel(
                id = index,
                date = calculatedDate,
                isSelected = false,
                isCurrentMonthDate = previousMonth.month == month.month,
                todayTotalExpense = totalExpense,
                todayTotalIncome = totalIncome,
                todayTransactions = todayTransactions
            )
        }
        val numberOfDaysInCurrentMonth = List(month.month.maxLength()) {
            index++
            val calculatedDate = month.plusDays(it.toLong())
            val todayTransactions = transactions[calculatedDate]
            val totalExpense = todayTransactions?.sumOf { transaction ->
                if (transaction.categoryType == CategoryType.EXPENSE) transaction.amount else 0.0
            } ?: 0.0
            val totalIncome = todayTransactions?.sumOf { transaction ->
                if (transaction.categoryType == CategoryType.INCOME) transaction.amount else 0.0
            } ?: 0.0
            CalendarDateModel(
                id = index,
                date = calculatedDate,
                isSelected = calculatedDate.dayOfMonth == todayDate.dayOfMonth,
                isCurrentMonthDate = LocalDate.now().month == month.month,
                todayTotalExpense = totalExpense,
                todayTotalIncome = totalIncome,
                todayTransactions = todayTransactions
            )
        }
        val numberOfDaysInNextMonth = List(nextMonthDays) {
            index++
            val calculatedDate = nextMonth.plusDays(it.toLong())
            val todayTransactions = transactions[calculatedDate]
            val totalExpense = todayTransactions?.sumOf { transaction ->
                if (transaction.categoryType == CategoryType.EXPENSE) transaction.amount else 0.0
            } ?: 0.0
            val totalIncome = todayTransactions?.sumOf { transaction ->
                if (transaction.categoryType == CategoryType.INCOME) transaction.amount else 0.0
            } ?: 0.0
            CalendarDateModel(
                id = index,
                date = calculatedDate,
                isSelected = false,
                isCurrentMonthDate = nextMonth.month == month.month,
                todayTotalExpense = totalExpense,
                todayTotalIncome = totalIncome,
                todayTransactions = todayTransactions
            )
        }

        return numberOfDaysInPreviousMonth + numberOfDaysInCurrentMonth + numberOfDaysInNextMonth
    }

    fun updateTo2Digits(hour: Int, minute: Int): String {
        val twoDigitHours = if (hour < 10) "0$hour" else hour
        val twoDigitMinutes = if (minute < 10) "0$minute" else minute
        return "$twoDigitHours:$twoDigitMinutes"
    }

    fun getTotal(transactions: List<TransactionModel>, type: CategoryType): Double =
        transactions.sumOf { if (it.categoryType == type) it.amount else 0.0 }
}