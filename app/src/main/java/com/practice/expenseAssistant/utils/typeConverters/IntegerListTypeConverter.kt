package com.practice.expenseAssistant.utils.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntegerListTypeConverter {

    @TypeConverter
    fun listOfIntToString(intList: List<Int>): String = Gson().toJson(intList)

    @TypeConverter
    fun stringToListOfInt(list: String): List<Int> = Gson().fromJson(
        list,
        object : TypeToken<List<Int>>() {}.type
    )
}