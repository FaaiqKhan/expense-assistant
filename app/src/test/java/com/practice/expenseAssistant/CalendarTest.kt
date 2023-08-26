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

    @Test
    fun climbStairs() {
        val count = climbStairs(5)
        println("count is: $count")
    }

    private fun climbStairs(n: Int): Int {
        if (n <= 2) return n

        var maxSteps = 2; var minSteps = 1

        repeat (times = n - 2) {
            val totalSteps = maxSteps + minSteps
            minSteps = maxSteps
            maxSteps = totalSteps
        }

        return maxSteps
    }

    @Test
    fun merge() {
        val nums1 = mutableListOf(1,2,3,0,0,0); val m = 3; val nums2 = listOf(2,5,6); val n = 3
        val merged = merge(nums1, m, nums2, n)
        println("merged list is: $merged")
    }

    private fun merge(nums1: MutableList<Int>, m: Int, nums2: List<Int>, n: Int) {
        if (n == 0) return

        var nums1Index = m - 1
        var nums2Index = n - 1

        for (i in (nums1.size - 1) downTo 0) {
            val a = nums1[nums1Index]
            val b = nums2[nums2Index]
            nums1[i] = if (nums1Index >= 0 && a >= b) {
                nums1Index--
                a
            } else {
                nums2Index--
                b
            }
        }
    }

    @Test
    fun pascalTriangle() {
        val result = generate(5)
        println("Pascal triangle is: $result")
    }

    private fun generate(numRows: Int): List<List<Int>> {
        if (numRows == 1) return listOf(listOf(1))
        if (numRows == 2) return listOf(listOf(1), listOf(1, 1))
        val result = mutableListOf(listOf(1), listOf(1, 1))
        for (i in 1..(numRows - 2)) {
            var p1 = 0; var p2 = 1
            val expectedValue = mutableListOf(1)
            while (p2 < result[i].size) {
                expectedValue.add(result[i][p1++] + result[i][p2++])
            }
            expectedValue.add(1)
            result.add(expectedValue)
        }
        return result
    }

    @Test
    fun pascalTriangleRowIndex() {
        val result = getRow(30)
        println("Pascal triangle is: $result")
    }

    private fun getRow(rowIndex: Int): List<Int> {
        // TODO: To find pascal's triangle nth row use the following formula
        // nCr = (nCr - 1 * (n - r + 1)) / r
        // here: n is required row, r is iteration value
        val result = mutableListOf<Long>(1)
        for (i in 1..rowIndex) {
            if (i > 14) {
                println()
            }
            val value = (result[i - 1] * (rowIndex - i + 1)) / i
            result.add(value)
        }
        return result.map { it.toInt() }
    }
}