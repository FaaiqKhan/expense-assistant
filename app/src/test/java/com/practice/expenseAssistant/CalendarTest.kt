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
                note = "",
                amount = 15.0,
                date = monthOfJuly.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val augustDates = List(monthOfAugust.month.maxLength()) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                note = "",
                amount = ((it + 1) * 20).toDouble(),
                date = monthOfAugust.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val septemberDates = List(5) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                note = "",
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

    @Test
    fun searchInsert() {
        val nums = listOf(1,3,5,6); val target = 0
        val index = binarySearch(nums, target)
        assertEquals(index, 0)
    }

    private fun binarySearch(nums: List<Int>, target: Int): Int {
        if (nums.first() == 0 || nums.first() > target) return 0
        if (nums.last() < target) return nums.size
        var min = 0; var max = nums.size - 1
        while (min <= max) {
            val pivot = min + (max - min) / 2
            if (target == nums[pivot]) return pivot
            if (target > nums[pivot]) min = pivot + 1
            else max = pivot - 1
        }
        return min
    }

    @Test
    fun lengthOfLastWord() {
        val s = "a "
        val count = lengthOfLastWord(s)
        assertEquals(1, count)
    }
    private fun lengthOfLastWord(s: String): Int {
        if (s.isEmpty()) return 0
        var count = 0
        for (i in (s.length - 1) downTo 0) {
            if (s[i] == ' ') {
                if (count == 0) continue
                else break
            }
            else count++
        }
        return count
    }
}