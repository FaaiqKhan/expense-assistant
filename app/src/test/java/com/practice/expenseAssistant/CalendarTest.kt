package com.practice.expenseAssistant

import com.practice.expenseAssistant.data.CalendarDateModel
import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.utils.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class CalendarTest {

    @Test
    fun calendar_dates_test() {
        val monthOfJuly = LocalDate.of(2023, 6, 25)
        val monthOfAugust = LocalDate.of(2023, 7, 1)
        val monthOfSeptember = LocalDate.of(2023, 8, 1)

        val julyDates = List(6) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                amount = 15.0,
                date = monthOfJuly.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val augustDates = List(monthOfAugust.month.maxLength()) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                amount = ((it + 1) * 20).toDouble(),
                date = monthOfAugust.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val septemberDates = List(5) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                amount = ((it + 1) * 20).toDouble(),
                date = monthOfSeptember.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val expectedDates = julyDates + augustDates + septemberDates
        val dates: List<CalendarDateModel> =
            Utils.createCalenderDays(month = monthOfAugust, todayDate = LocalDate.now())
        assertEquals(dates.size, expectedDates.size)
    }
}