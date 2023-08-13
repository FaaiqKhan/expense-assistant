package com.practice.expenseAssistant.utils.typeConverters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateTypeConverter {

    @TypeConverter
    fun localDateFromTimestamp(value: Long): LocalDate = LocalDate.ofEpochDay(value)

    @TypeConverter
    fun localDateToTimestamp(localDate: LocalDate): Long = localDate.toEpochDay()
}