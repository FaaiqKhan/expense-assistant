package com.practice.expenseAssistant.data.datasource.database.typeConverters

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeTypeConverter {

    @TypeConverter
    fun localTimeFromTimestamp(value: Long): LocalTime = LocalTime.ofNanoOfDay(value)

    @TypeConverter
    fun localTimeToTimestamp(localTime: LocalTime): Long = localTime.toNanoOfDay()
}