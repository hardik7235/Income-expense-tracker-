package com.example.ui.viewmodel

import androidx.compose.ui.graphics.Color
import com.example.data.model.Transaction
import com.example.data.model.TransactionType
import com.example.data.model.User

data class CategoryExpenseSummary(
    val categoryName: String,
    val totalAmount: Double,
    val percentage: Float, // 0f to 100f
    val transactionCount: Int,
    val iconName: String,
    val color: Color
)

data class DailyTrendData(
    val dayOfMonth: Int,
    val dayLabel: String,
    val incomeAmount: Double,
    val expenseAmount: Double
)

data class PaymentMethodSummary(
    val method: String,
    val amount: Double,
    val count: Int,
    val percentage: Float
)

data class FinanceUiState(
    val currentUser: User? = null,
    val isAuthenticated: Boolean = true,
    val allUsers: List<User> = emptyList(),
    val authErrorMessage: String? = null,
    val isAuthLoading: Boolean = false,
    val isProfileDialogOpen: Boolean = false,
    val selectedYear: Int = 2026,
    val selectedMonth: Int = 7, // 0-indexed: 7 is August
    val selectedMonthLabel: String = "August 2026",
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val netBalance: Double = 0.0,
    val overallAllTimeBalance: Double = 0.0,
    val monthlyBudget: Double = 50000.0,
    val budgetUsedPercentage: Float = 0f,
    val savingsRate: Float = 0f,
    val dailyAverageExpense: Double = 0.0,
    val currencySymbol: String = "₹",
    val expenseCategories: List<CategoryExpenseSummary> = emptyList(),
    val incomeCategories: List<CategoryExpenseSummary> = emptyList(),
    val dailyTrends: List<DailyTrendData> = emptyList(),
    val paymentBreakdown: List<PaymentMethodSummary> = emptyList(),
    val searchQuery: String = "",
    val filterType: TransactionType? = null,
    val selectedCategoryFilter: String? = null,
    val isAddEditSheetOpen: Boolean = false,
    val editingTransaction: Transaction? = null,
    val currentTab: Int = 0 // 0 = Home, 1 = Transactions, 2 = Monthly Report, 3 = Analytics
)
