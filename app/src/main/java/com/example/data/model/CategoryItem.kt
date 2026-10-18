package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryItem(
    val name: String,
    val type: TransactionType,
    val iconName: String,
    val colorHex: Long
) {
    val color: Color get() = Color(colorHex)

    val icon: ImageVector
        get() = when (iconName) {
            "work" -> Icons.Default.Work
            "computer" -> Icons.Default.Computer
            "trending_up" -> Icons.Default.TrendingUp
            "business" -> Icons.Default.Business
            "apartment" -> Icons.Default.Apartment
            "card_giftcard" -> Icons.Default.CardGiftcard
            "savings" -> Icons.Default.Savings
            "restaurant" -> Icons.Default.Restaurant
            "shopping_cart" -> Icons.Default.ShoppingCart
            "shopping_bag" -> Icons.Default.ShoppingBag
            "home" -> Icons.Default.Home
            "directions_car" -> Icons.Default.DirectionsCar
            "bolt" -> Icons.Default.Bolt
            "movie" -> Icons.Default.Movie
            "local_hospital" -> Icons.Default.LocalHospital
            "flight" -> Icons.Default.Flight
            "school" -> Icons.Default.School
            "spa" -> Icons.Default.Spa
            "subscriptions" -> Icons.Default.Subscriptions
            "receipt_long" -> Icons.Default.ReceiptLong
            "fitness_center" -> Icons.Default.FitnessCenter
            "pets" -> Icons.Default.Pets
            "payments" -> Icons.Default.Payments
            else -> if (type == TransactionType.INCOME) Icons.Default.AccountBalanceWallet else Icons.Default.CreditCard
        }

    companion object {
        val DEFAULT_INCOME_CATEGORIES = listOf(
            CategoryItem("Salary", TransactionType.INCOME, "work", 0xFF10B981),
            CategoryItem("Freelance", TransactionType.INCOME, "computer", 0xFF06B6D4),
            CategoryItem("Investments", TransactionType.INCOME, "trending_up", 0xFF8B5CF6),
            CategoryItem("Business", TransactionType.INCOME, "business", 0xFF3B82F6),
            CategoryItem("Rental", TransactionType.INCOME, "apartment", 0xFF14B8A6),
            CategoryItem("Gifts & Bonus", TransactionType.INCOME, "card_giftcard", 0xFFEC4899),
            CategoryItem("Savings", TransactionType.INCOME, "savings", 0xFFF59E0B),
            CategoryItem("Other Income", TransactionType.INCOME, "payments", 0xFF6366F1)
        )

        val DEFAULT_EXPENSE_CATEGORIES = listOf(
            CategoryItem("Food & Dining", TransactionType.EXPENSE, "restaurant", 0xFFF43F5E),
            CategoryItem("Groceries", TransactionType.EXPENSE, "shopping_cart", 0xFFF97316),
            CategoryItem("Shopping", TransactionType.EXPENSE, "shopping_bag", 0xFFEC4899),
            CategoryItem("Housing & Rent", TransactionType.EXPENSE, "home", 0xFF8B5CF6),
            CategoryItem("Transportation", TransactionType.EXPENSE, "directions_car", 0xFF3B82F6),
            CategoryItem("Utilities & Bills", TransactionType.EXPENSE, "bolt", 0xFFEAB308),
            CategoryItem("Entertainment", TransactionType.EXPENSE, "movie", 0xFFA855F7),
            CategoryItem("Health & Medical", TransactionType.EXPENSE, "local_hospital", 0xFFEF4444),
            CategoryItem("Travel & Vacation", TransactionType.EXPENSE, "flight", 0xFF06B6D4),
            CategoryItem("Education", TransactionType.EXPENSE, "school", 0xFF6366F1),
            CategoryItem("Fitness & Sport", TransactionType.EXPENSE, "fitness_center", 0xFF10B981),
            CategoryItem("Subscriptions", TransactionType.EXPENSE, "subscriptions", 0xFFD946EF),
            CategoryItem("Other Expense", TransactionType.EXPENSE, "receipt_long", 0xFF64748B)
        )

        fun getCategoryByName(name: String, type: TransactionType): CategoryItem {
            val list = if (type == TransactionType.INCOME) DEFAULT_INCOME_CATEGORIES else DEFAULT_EXPENSE_CATEGORIES
            return list.find { it.name.equals(name, ignoreCase = true) }
                ?: CategoryItem(
                    name = name,
                    type = type,
                    iconName = if (type == TransactionType.INCOME) "payments" else "receipt_long",
                    colorHex = if (type == TransactionType.INCOME) 0xFF10B981 else 0xFFF43F5E
                )
        }
    }
}
