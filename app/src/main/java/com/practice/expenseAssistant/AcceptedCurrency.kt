package com.practice.expenseAssistant

sealed class AcceptedCurrency {
    abstract val valueInDollars: Float

    class Dollars : AcceptedCurrency() {

        override val valueInDollars: Float = 1.0F
    }

    class Euro : AcceptedCurrency() {

        override val valueInDollars: Float = 1.25F
    }

    class Crypto : AcceptedCurrency() {

        override val valueInDollars: Float = 2534.53F
    }

    val name: String
        get() = when (this) {
            is Dollars -> "Dollars"
            is Euro -> "Euro"
            is Crypto -> "NerdCoin"
        }

    var amount: Float = 0.0F

    fun totalValueInDollars(): Float = amount * valueInDollars
}