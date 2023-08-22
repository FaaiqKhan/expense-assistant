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
                amount = 15,
                date = monthOfJuly.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val augustDates = List(monthOfAugust.month.maxLength()) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                note = "",
                amount = (it + 1) * 20,
                date = monthOfAugust.plusDays(it.toLong()),
                time = LocalTime.now()
            )
        }

        val septemberDates = List(5) {
            TransactionModel(
                categoryType = CategoryType.EXPENSE,
                category = ExpenseType.BILL,
                note = "",
                amount = (it + 1) * 20,
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
        val nums = listOf(1,3,7); val target = 2
        val index = searchInsert(nums, target)
        assertEquals(1, index)
    }

    private fun searchInsert(nums: List<Int>, target: Int): Int {
        if (target <= nums.first()) return 0
        if (target >= nums.last()) return nums.size

        var index = 0; var pivot = nums.size / 2
        if (nums[pivot] < target) { index = pivot + 1; pivot = nums.size }

        for (i in index until pivot) {
            val a = nums[i]
            if (a >= target) return i
        }

        return pivot
    }

    private fun searchInsert11(nums: IntArray, target: Int): Int {
        if (target == 0) return 0
        var index = 0
        var pivot = nums.size / 2
        if (nums[pivot] < target) {
            index = pivot + 1
            pivot = nums.size
        }
        for (i in index until pivot) {
            if (nums[i] == target || nums[i] - target == 1) return i
            if (nums[i] - target == -1) return i+1
        }
        return pivot
    }
}