package com.practice.expenseAssistant.data

import java.time.LocalDate

data class CalendarDataModel(
    val localDate: LocalDate,
    val localCalendar: List<CalendarDateModel>
)
