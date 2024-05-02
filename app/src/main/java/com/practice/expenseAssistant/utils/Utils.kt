package com.practice.expenseAssistant.utils

import com.practice.expenseAssistant.data.CalendarDateModel
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.data.datasource.database.entities.Transaction
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
        year: Int,
        month: Int,
        date: Int,
        transactions: Map<LocalDate, List<TransactionModel>> = mapOf()
    ): List<CalendarDateModel> {
        val localDate = LocalDate.of(year, month, 1)
        val daysToAdvance = daysMap.getValue(localDate.dayOfWeek.name)

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
                isCurrentMonthDate = previousMonth.month == localDate.month,
                todayTotalExpense = totalExpense,
                todayTotalIncome = totalIncome,
                todayTransactions = todayTransactions
            )
        }
        val numberOfDaysInCurrentMonth = List(localDate.month.maxLength()) {
            index++
            val calculatedDate = localDate.plusDays(it.toLong())
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
                isSelected = calculatedDate.dayOfMonth == date,
                isCurrentMonthDate = previousMonth.monthValue != month && nextMonth.monthValue != month,
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
                isCurrentMonthDate = nextMonth.monthValue == month,
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

    fun parseTransactions(transactions: List<Transaction>): Map<LocalDate, MutableList<TransactionModel>> {
        val transactionsData: MutableMap<LocalDate, MutableList<TransactionModel>> = mutableMapOf()
        transactions.forEach {
            val transaction = convertTransactionToTransactionModel(it)
            if (transactionsData[it.date] != null) {
                transactionsData.getValue(it.date).add(transaction)
            } else {
                transactionsData[it.date] = mutableListOf(transaction)
            }
        }
        return transactionsData
    }

    fun parseTransactions2(transactions: List<TransactionModel>): Map<LocalDate, MutableList<TransactionModel>> {
        val transactionsData: MutableMap<LocalDate, MutableList<TransactionModel>> = mutableMapOf()
        transactions.forEach {
            if (transactionsData[it.date] != null) {
                transactionsData.getValue(it.date).add(it)
            } else {
                transactionsData[it.date] = mutableListOf(it)
            }
        }
        return transactionsData
    }

    fun convertTransactionToTransactionModel(transaction: Transaction): TransactionModel {
        return TransactionModel(
            categoryType = transaction.categoryType,
            category = transaction.category,
            note = transaction.note,
            amount = transaction.amount,
            date = transaction.date,
            time = transaction.time,
            month = transaction.month,
            year = transaction.year
        )
    }

    fun decapitalizeStringExpectFirstLetter(string: String): String {
        var decapitalize = false
        val stringBuilder = StringBuilder()
        string.forEach {
            if (decapitalize) {
                stringBuilder.append(it.lowercase())
            } else {
                decapitalize = true
                stringBuilder.append(it)
            }
        }
        return stringBuilder.toString()
    }
}