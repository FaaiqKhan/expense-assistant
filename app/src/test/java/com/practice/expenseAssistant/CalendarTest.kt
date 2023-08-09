package com.practice.expenseAssistant

import com.practice.expenseAssistant.data.TransactionModel
import com.practice.expenseAssistant.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class CalendarTest {

    @Test
    fun calendar_dates_test() {
        val monthOfJuly = LocalDate.of(2023, 6, 25)
        val monthOfAugust = LocalDate.of(2023, 7, 1)
        val monthOfSeptember = LocalDate.of(2023, 8, 1)

        val julyDates = List(6) {
            TransactionModel(
                date = monthOfJuly.plusDays(it.toLong()),
                isSelected = false,
                isCurrentMonthDate = false
            )
        }

        val augustDates = List(monthOfAugust.month.maxLength()) {
            TransactionModel(
                date = monthOfAugust.plusDays(it.toLong()),
                amount = ((it + 1) * 20).toString(),
                isSelected = LocalDate.now().dayOfMonth == it + 1,
                isCurrentMonthDate = true
            )
        }

        val septemberDates = List(5) {
            TransactionModel(
                date = monthOfSeptember.plusDays(it.toLong()),
                isSelected = false,
                isCurrentMonthDate = false
            )
        }

        val expectedDates = julyDates + augustDates + septemberDates
        val dates: List<TransactionModel> = Utils.createCalenderDays(monthOfAugust)
        assertEquals(dates.size, expectedDates.size)
    }
}