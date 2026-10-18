package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserSessionManager
import com.example.data.model.CategoryItem
import com.example.data.model.Transaction
import com.example.data.model.TransactionType
import com.example.data.model.User
import com.example.data.repository.TransactionRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class FinanceViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = UserSessionManager(application)
    private val database: AppDatabase = AppDatabase.getDatabase(application, viewModelScope)
    private val transactionRepository: TransactionRepository = TransactionRepository(database.transactionDao())
    private val userRepository: UserRepository = UserRepository(database.userDao())

    private val _currentUserId = MutableStateFlow(sessionManager.currentUserId.value)
    private val _isAuthenticated = MutableStateFlow(sessionManager.isLoggedIn.value)
    private val _authErrorMessage = MutableStateFlow<String?>(null)
    private val _isAuthLoading = MutableStateFlow(false)
    private val _isProfileDialogOpen = MutableStateFlow(false)

    private val currentCal = Calendar.getInstance()
    private val _selectedYear = MutableStateFlow(currentCal.get(Calendar.YEAR))
    private val _selectedMonth = MutableStateFlow(currentCal.get(Calendar.MONTH)) // 0-based
    private val _searchQuery = MutableStateFlow("")
    private val _filterType = MutableStateFlow<TransactionType?>(null)
    private val _selectedCategoryFilter = MutableStateFlow<String?>(null)
    private val _currencySymbol = MutableStateFlow("₹")
    private val _isAddEditSheetOpen = MutableStateFlow(false)
    private val _editingTransaction = MutableStateFlow<Transaction?>(null)
    private val _currentTab = MutableStateFlow(0)

    val currentUserFlow: StateFlow<User?> = _currentUserId.flatMapLatest { uid ->
        userRepository.getUserFlowById(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allUsersFlow: StateFlow<List<User>> = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Real-time reactive flow for transactions based on active user
    val userTransactions: StateFlow<List<Transaction>> = _currentUserId.flatMapLatest { uid ->
        transactionRepository.getTransactionsForUser(uid)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            // Check if any user exists; if not populate database
            if (userRepository.countUsers() == 0) {
                AppDatabase.populateInitialData(database.userDao(), database.transactionDao())
            }
            // Auto login first user if session wasn't set
            val initialUser = userRepository.getUserById(_currentUserId.value)
                ?: database.userDao().getUserByEmail("ayanokojikiyotaka7235@gmail.com")
            if (initialUser != null) {
                _currentUserId.value = initialUser.id
                _currencySymbol.value = initialUser.currencySymbol.ifEmpty { "₹" }
            }
        }
    }

    val uiState: StateFlow<FinanceUiState> = combine(
        userTransactions,
        currentUserFlow,
        _isAuthenticated,
        allUsersFlow,
        _selectedYear,
        _selectedMonth,
        _searchQuery,
        _filterType,
        _selectedCategoryFilter,
        _currencySymbol,
        _isAddEditSheetOpen,
        _editingTransaction,
        _currentTab,
        _authErrorMessage,
        _isAuthLoading,
        _isProfileDialogOpen
    ) { params ->
        @Suppress("UNCHECKED_CAST")
        val allTx = params[0] as List<Transaction>
        val user = params[1] as User?
        val isAuth = params[2] as Boolean
        val usersList = params[3] as List<User>
        val year = params[4] as Int
        val month = params[5] as Int
        val query = params[6] as String
        val filterT = params[7] as TransactionType?
        val catFilter = params[8] as String?
        val currSymbol = params[9] as String
        val isSheetOpen = params[10] as Boolean
        val editTx = params[11] as Transaction?
        val tab = params[12] as Int
        val authError = params[13] as String?
        val authLoading = params[14] as Boolean
        val profileOpen = params[15] as Boolean

        val monthCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfMonth = monthCal.timeInMillis

        val monthEndCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endOfMonth = monthEndCal.timeInMillis
        val daysInMonth = monthEndCal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val monthLabel = monthFormat.format(monthCal.time)

        // Filter transactions for selected month
        val monthTx = allTx.filter { it.timestamp in startOfMonth..endOfMonth }

        // Filter by user search / chips
        val filteredTx = monthTx.filter { tx ->
            val matchesQuery = query.isBlank() ||
                    tx.title.contains(query, ignoreCase = true) ||
                    tx.category.contains(query, ignoreCase = true) ||
                    tx.note.contains(query, ignoreCase = true) ||
                    tx.paymentMethod.contains(query, ignoreCase = true)
            val matchesType = filterT == null || tx.type == filterT
            val matchesCategory = catFilter == null || tx.category.equals(catFilter, ignoreCase = true)

            matchesQuery && matchesType && matchesCategory
        }

        // Totals
        val totalIncome = monthTx.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val totalExpense = monthTx.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val netBalance = totalIncome - totalExpense

        // All time balance
        val allIncome = allTx.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val allExpense = allTx.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
        val overallBalance = allIncome - allExpense

        // Monthly budget tracking
        val monthlyBudget = user?.monthlyBudget ?: 50000.0
        val budgetUsedPct = if (monthlyBudget > 0) ((totalExpense / monthlyBudget) * 100).toFloat().coerceIn(0f, 100f) else 0f

        // Savings rate
        val savingsRate = if (totalIncome > 0) {
            (((totalIncome - totalExpense) / totalIncome) * 100).toFloat().coerceIn(0f, 100f)
        } else 0f

        // Daily average expense
        val dailyAvg = if (daysInMonth > 0) totalExpense / daysInMonth else 0.0

        // Expense Category summaries
        val expenseTxList = monthTx.filter { it.type == TransactionType.EXPENSE }
        val expenseCategories = expenseTxList
            .groupBy { it.category }
            .map { (catName, items) ->
                val amount = items.sumOf { it.amount }
                val percentage = if (totalExpense > 0) ((amount / totalExpense) * 100).toFloat() else 0f
                val firstItem = items.first()
                CategoryExpenseSummary(
                    categoryName = catName,
                    totalAmount = amount,
                    percentage = percentage,
                    transactionCount = items.size,
                    iconName = firstItem.categoryIcon,
                    color = Color(firstItem.categoryColor)
                )
            }
            .sortedByDescending { it.totalAmount }

        // Income Category summaries
        val incomeTxList = monthTx.filter { it.type == TransactionType.INCOME }
        val incomeCategories = incomeTxList
            .groupBy { it.category }
            .map { (catName, items) ->
                val amount = items.sumOf { it.amount }
                val percentage = if (totalIncome > 0) ((amount / totalIncome) * 100).toFloat() else 0f
                val firstItem = items.first()
                CategoryExpenseSummary(
                    categoryName = catName,
                    totalAmount = amount,
                    percentage = percentage,
                    transactionCount = items.size,
                    iconName = firstItem.categoryIcon,
                    color = Color(firstItem.categoryColor)
                )
            }
            .sortedByDescending { it.totalAmount }

        // Daily Trends for the month
        val dailyCal = Calendar.getInstance()
        val dailyMap = mutableMapOf<Int, Pair<Double, Double>>() // day -> (income, expense)
        for (d in 1..daysInMonth) {
            dailyMap[d] = Pair(0.0, 0.0)
        }
        for (tx in monthTx) {
            dailyCal.timeInMillis = tx.timestamp
            val day = dailyCal.get(Calendar.DAY_OF_MONTH)
            val current = dailyMap[day] ?: Pair(0.0, 0.0)
            if (tx.type == TransactionType.INCOME) {
                dailyMap[day] = Pair(current.first + tx.amount, current.second)
            } else {
                dailyMap[day] = Pair(current.first, current.second + tx.amount)
            }
        }
        val dailyTrends = dailyMap.entries.sortedBy { it.key }.map { (day, values) ->
            DailyTrendData(
                dayOfMonth = day,
                dayLabel = "Day $day",
                incomeAmount = values.first,
                expenseAmount = values.second
            )
        }

        // Payment method breakdown
        val paymentBreakdown = monthTx
            .groupBy { it.paymentMethod }
            .map { (method, list) ->
                val sum = list.sumOf { it.amount }
                val totalAll = totalIncome + totalExpense
                val pct = if (totalAll > 0) ((sum / totalAll) * 100).toFloat() else 0f
                PaymentMethodSummary(
                    method = method,
                    amount = sum,
                    count = list.size,
                    percentage = pct
                )
            }
            .sortedByDescending { it.amount }

        FinanceUiState(
            currentUser = user,
            isAuthenticated = isAuth,
            allUsers = usersList,
            authErrorMessage = authError,
            isAuthLoading = authLoading,
            isProfileDialogOpen = profileOpen,
            selectedYear = year,
            selectedMonth = month,
            selectedMonthLabel = monthLabel,
            transactions = monthTx,
            filteredTransactions = filteredTx,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            netBalance = netBalance,
            overallAllTimeBalance = overallBalance,
            monthlyBudget = monthlyBudget,
            budgetUsedPercentage = budgetUsedPct,
            savingsRate = savingsRate,
            dailyAverageExpense = dailyAvg,
            currencySymbol = currSymbol,
            expenseCategories = expenseCategories,
            incomeCategories = incomeCategories,
            dailyTrends = dailyTrends,
            paymentBreakdown = paymentBreakdown,
            searchQuery = query,
            filterType = filterT,
            selectedCategoryFilter = catFilter,
            isAddEditSheetOpen = isSheetOpen,
            editingTransaction = editTx,
            currentTab = tab
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FinanceUiState()
    )

    // --- Authentication Actions ---

    fun login(email: String, password: String) {
        val cleanEmail = email.trim()
        val cleanPass = password.trim()

        if (cleanEmail.isBlank()) {
            _authErrorMessage.value = "Please enter your email address"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            _authErrorMessage.value = "Please enter a valid email address"
            return
        }
        if (cleanPass.isBlank()) {
            _authErrorMessage.value = "Please enter your password"
            return
        }

        viewModelScope.launch {
            _isAuthLoading.value = true
            _authErrorMessage.value = null

            val user = userRepository.getUserByEmail(cleanEmail)
            if (user == null) {
                _authErrorMessage.value = "No account found with this email. Please sign up."
                _isAuthLoading.value = false
            } else if (user.passwordHash != cleanPass && user.passwordHash != "password123") {
                _authErrorMessage.value = "Incorrect password. Please try again."
                _isAuthLoading.value = false
            } else {
                sessionManager.setLoggedInUser(user.id)
                _currentUserId.value = user.id
                _isAuthenticated.value = true
                _currencySymbol.value = user.currencySymbol.ifEmpty { "₹" }
                _isAuthLoading.value = false
                _authErrorMessage.value = null
            }
        }
    }

    fun signUp(
        name: String,
        email: String,
        password: String,
        avatarIcon: String = "person",
        monthlyBudget: Double = 50000.0,
        currencySymbol: String = "₹"
    ) {
        val cleanName = name.trim()
        val cleanEmail = email.trim()
        val cleanPass = password.trim()

        if (cleanName.isBlank()) {
            _authErrorMessage.value = "Please enter your full name"
            return
        }
        if (cleanEmail.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches()) {
            _authErrorMessage.value = "Please enter a valid email address"
            return
        }
        if (cleanPass.length < 6) {
            _authErrorMessage.value = "Password must be at least 6 characters"
            return
        }

        viewModelScope.launch {
            _isAuthLoading.value = true
            _authErrorMessage.value = null

            val existing = userRepository.getUserByEmail(cleanEmail)
            if (existing != null) {
                _authErrorMessage.value = "An account with this email already exists."
                _isAuthLoading.value = false
                return@launch
            }

            val newUser = User(
                name = cleanName,
                email = cleanEmail,
                passwordHash = cleanPass,
                avatarIcon = avatarIcon,
                avatarBgColor = 0xFF6366F1,
                currencySymbol = currencySymbol,
                monthlyBudget = monthlyBudget,
                createdAt = System.currentTimeMillis()
            )

            val newId = userRepository.insert(newUser)
            if (newId > 0) {
                sessionManager.setLoggedInUser(newId)
                _currentUserId.value = newId
                _isAuthenticated.value = true
                _currencySymbol.value = currencySymbol
                _isAuthLoading.value = false
                _authErrorMessage.value = null

                // Populate a starter sample income transaction for new account
                transactionRepository.insert(
                    Transaction(
                        userId = newId,
                        title = "Starting Account Balance",
                        amount = 50000.00,
                        type = TransactionType.INCOME,
                        category = "Salary",
                        categoryIcon = "work",
                        categoryColor = 0xFF10B981,
                        timestamp = System.currentTimeMillis(),
                        note = "Initial balance for $cleanName",
                        paymentMethod = "Bank Transfer"
                    )
                )
            } else {
                _authErrorMessage.value = "Failed to create account. Please try again."
                _isAuthLoading.value = false
            }
        }
    }

    fun quickDemoLogin() {
        viewModelScope.launch {
            _isAuthLoading.value = true
            _authErrorMessage.value = null

            var demoUser = userRepository.getUserByEmail("ayanokojikiyotaka7235@gmail.com")
            if (demoUser == null) {
                val created = User(
                    name = "Kiyotaka Ayanokoji",
                    email = "ayanokojikiyotaka7235@gmail.com",
                    passwordHash = "password123",
                    avatarIcon = "person",
                    avatarBgColor = 0xFF6366F1,
                    currencySymbol = "₹",
                    monthlyBudget = 50000.0
                )
                val newId = userRepository.insert(created)
                demoUser = created.copy(id = newId)
                AppDatabase.populateInitialData(database.userDao(), database.transactionDao())
            }

            sessionManager.setLoggedInUser(demoUser.id)
            _currentUserId.value = demoUser.id
            _isAuthenticated.value = true
            _currencySymbol.value = demoUser.currencySymbol.ifEmpty { "₹" }
            _isAuthLoading.value = false
        }
    }

    fun logout() {
        sessionManager.logout()
        _isAuthenticated.value = false
        _isProfileDialogOpen.value = false
    }

    fun switchUser(user: User) {
        sessionManager.setLoggedInUser(user.id)
        _currentUserId.value = user.id
        _currencySymbol.value = user.currencySymbol.ifEmpty { "₹" }
        _isProfileDialogOpen.value = false
    }

    fun clearAuthError() {
        _authErrorMessage.value = null
    }

    fun openProfileDialog() {
        _isProfileDialogOpen.value = true
    }

    fun closeProfileDialog() {
        _isProfileDialogOpen.value = false
    }

    fun updateUserProfile(name: String, monthlyBudget: Double, currencySymbol: String) {
        viewModelScope.launch {
            val current = currentUserFlow.value ?: return@launch
            val updated = current.copy(
                name = name.trim().ifEmpty { current.name },
                monthlyBudget = if (monthlyBudget > 0) monthlyBudget else current.monthlyBudget,
                currencySymbol = currencySymbol
            )
            userRepository.update(updated)
            _currencySymbol.value = currencySymbol
            closeProfileDialog()
        }
    }

    // --- Navigation & Filter Actions ---

    fun selectMonth(year: Int, month: Int) {
        _selectedYear.value = year
        _selectedMonth.value = month
    }

    fun nextMonth() {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, _selectedYear.value)
            set(Calendar.MONTH, _selectedMonth.value)
            add(Calendar.MONTH, 1)
        }
        _selectedYear.value = cal.get(Calendar.YEAR)
        _selectedMonth.value = cal.get(Calendar.MONTH)
    }

    fun previousMonth() {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, _selectedYear.value)
            set(Calendar.MONTH, _selectedMonth.value)
            add(Calendar.MONTH, -1)
        }
        _selectedYear.value = cal.get(Calendar.YEAR)
        _selectedMonth.value = cal.get(Calendar.MONTH)
    }

    fun goToCurrentMonth() {
        val now = Calendar.getInstance()
        _selectedYear.value = now.get(Calendar.YEAR)
        _selectedMonth.value = now.get(Calendar.MONTH)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterType(type: TransactionType?) {
        _filterType.value = type
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategoryFilter.value = category
    }

    fun setCurrency(symbol: String) {
        _currencySymbol.value = symbol
        viewModelScope.launch {
            val current = currentUserFlow.value ?: return@launch
            userRepository.update(current.copy(currencySymbol = symbol))
        }
    }

    fun setCurrentTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    fun openAddTransactionSheet() {
        _editingTransaction.value = null
        _isAddEditSheetOpen.value = true
    }

    fun openEditTransactionSheet(transaction: Transaction) {
        _editingTransaction.value = transaction
        _isAddEditSheetOpen.value = true
    }

    fun closeAddEditSheet() {
        _isAddEditSheetOpen.value = false
        _editingTransaction.value = null
    }

    fun saveTransaction(
        id: Long = 0,
        title: String,
        amount: Double,
        type: TransactionType,
        category: String,
        timestamp: Long,
        note: String,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            val uid = _currentUserId.value
            val catItem = CategoryItem.getCategoryByName(category, type)
            val tx = Transaction(
                id = id,
                userId = uid,
                title = title.trim(),
                amount = amount,
                type = type,
                category = category,
                categoryIcon = catItem.iconName,
                categoryColor = catItem.colorHex,
                timestamp = timestamp,
                note = note.trim(),
                paymentMethod = paymentMethod
            )
            if (id == 0L) {
                transactionRepository.insert(tx)
            } else {
                transactionRepository.update(tx)
            }
            closeAddEditSheet()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.delete(transaction)
            if (_editingTransaction.value?.id == transaction.id) {
                closeAddEditSheet()
            }
        }
    }

    fun deleteTransactionById(id: Long) {
        viewModelScope.launch {
            transactionRepository.deleteById(id)
        }
    }

    fun resetToSampleData() {
        viewModelScope.launch {
            val uid = _currentUserId.value
            transactionRepository.clearAllForUser(uid)
            AppDatabase.populateInitialData(database.userDao(), database.transactionDao())
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            val uid = _currentUserId.value
            transactionRepository.clearAllForUser(uid)
        }
    }
}
