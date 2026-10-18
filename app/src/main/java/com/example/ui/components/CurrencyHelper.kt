package com.example.ui.components

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object CurrencyHelper {
    val AVAILABLE_CURRENCIES = listOf(
        "₹" to "INR (₹) - Indian Rupee",
        "$" to "USD ($) - US Dollar",
        "€" to "EUR (€) - Euro",
        "£" to "GBP (£) - British Pound",
        "AED" to "AED (د.إ) - UAE Dirham",
        "¥" to "JPY (¥) - Japanese Yen",
        "A$" to "AUD (A$) - Australian Dollar",
        "C$" to "CAD (C$) - Canadian Dollar",
        "₩" to "KRW (₩) - Korean Won",
        "CHF" to "CHF - Swiss Franc",
        "R$" to "BRL (R$) - Brazilian Real"
    )

    fun format(amount: Double, symbol: String = "₹"): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,##0.00")
        return "$symbol${formatter.format(amount)}"
    }

    fun formatCompact(amount: Double, symbol: String = "₹"): String {
        return when {
            symbol == "₹" && amount >= 10_000_000 -> String.format(Locale.US, "%s%.2f Cr", symbol, amount / 10_000_000)
            symbol == "₹" && amount >= 100_000 -> String.format(Locale.US, "%s%.1f L", symbol, amount / 100_000)
            amount >= 1_000_000 -> String.format(Locale.US, "%s%.1fM", symbol, amount / 1_000_000)
            amount >= 1_000 -> String.format(Locale.US, "%s%.1fK", symbol, amount / 1_000)
            else -> format(amount, symbol)
        }
    }
}
