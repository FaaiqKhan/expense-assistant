package com.practice.expenseAssistant.data

import android.os.Parcel
import android.os.Parcelable
import com.practice.expenseAssistant.utils.*
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Suppress("DEPRECATION")
@Parcelize
data class TransactionModel(
    val categoryType: CategoryType,
    val category: Any,
    val amount: Double,
    val date: LocalDate,
    val time: LocalTime,
    val note: String = "",
    val edit: Boolean = false
) : Parcelable {

    private companion object : Parceler<TransactionModel> {

        override fun create(parcel: Parcel): TransactionModel {
            val type = parcel.readParcelable(CategoryType::class.java.classLoader)
                ?: CategoryType.EXPENSE
            val category = parcel.readString()!!
            return TransactionModel(
                categoryType = type,
                category = if (type == CategoryType.INCOME) {
                    IncomeType.valueOf(category)
                } else {
                    ExpenseType.valueOf(category)
                },
                amount = parcel.readDouble(),
                date = LocalDate.ofEpochDay(parcel.readLong()),
                time = LocalTime.ofNanoOfDay(parcel.readLong()),
                note = parcel.readString()!!,
                edit = parcel.readBoolean(),
            )
        }

        override fun TransactionModel.write(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(categoryType, flags)
            parcel.writeString(category.toString())
            parcel.writeDouble(amount)
            parcel.writeLong(date.toEpochDay())
            parcel.writeLong(time.toNanoOfDay())
            parcel.writeString(note)
            parcel.writeBoolean(edit)
        }

    }
}
