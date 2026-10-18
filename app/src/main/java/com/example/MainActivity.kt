package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddEditTransactionSheet
import com.example.ui.components.FrostedBackgroundWrapper
import com.example.ui.components.MonthYearPickerDialog
import com.example.ui.components.UserProfileDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.FrostedDarkBg
import com.example.ui.theme.FrostedEmeraldLight
import com.example.ui.theme.FrostedIndigo
import com.example.ui.theme.FrostedIndigoLight
import com.example.ui.theme.FrostedIndigoPill
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.FinanceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                FinanceApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceApp(viewModel: FinanceViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showMonthPicker by remember { mutableStateOf(false) }

    fun getAvatarVector(iconKey: String?): ImageVector {
        return when (iconKey) {
            "rocket" -> Icons.Default.RocketLaunch
            "savings" -> Icons.Default.Savings
            "trending" -> Icons.Default.TrendingUp
            "star" -> Icons.Default.Star
            "wallet" -> Icons.Default.AccountBalanceWallet
            else -> Icons.Default.Person
        }
    }

    if (!uiState.isAuthenticated) {
        AuthScreen(
            uiState = uiState,
            onLogin = { email, password -> viewModel.login(email, password) },
            onSignUp = { name, email, password, avatarIcon, budget, currency ->
                viewModel.signUp(name, email, password, avatarIcon, budget, currency)
            },
            onQuickDemoLogin = { viewModel.quickDemoLogin() },
            onClearError = { viewModel.clearAuthError() }
        )
    } else {
        FrostedBackgroundWrapper {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("finance_main_scaffold"),
                containerColor = Color.Transparent,
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            // User Profile Avatar Button in top left
                            Box(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(FrostedIndigoPill)
                                    .border(1.dp, FrostedIndigoLight.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                    .clickable { viewModel.openProfileDialog() }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                    .testTag("appbar_user_avatar_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = getAvatarVector(uiState.currentUser?.avatarIcon),
                                        contentDescription = "Profile",
                                        tint = FrostedIndigoLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = uiState.currentUser?.initials ?: "U",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                }
                            }
                        },
                        title = {
                            Text(
                                text = when (uiState.currentTab) {
                                    0 -> "RupeeFlow"
                                    1 -> "Transactions"
                                    2 -> "Monthly Reports"
                                    else -> "Analytics & Settings"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextWhite
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { showMonthPicker = true },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(38.dp)
                                    .background(Color(0x18FFFFFF), CircleShape)
                                    .border(1.dp, Color(0x22FFFFFF), CircleShape)
                                    .testTag("appbar_calendar_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Pick Month",
                                    tint = FrostedIndigoLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xEE0D111A),
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .border(width = 1.dp, color = GlassBorder)
                            .testTag("bottom_nav_bar")
                    ) {
                        val navItems = listOf(
                            Triple("Home", Icons.Filled.Home, Icons.Outlined.Home),
                            Triple("Records", Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong),
                            Triple("Reports", Icons.Filled.Assessment, Icons.Outlined.Assessment),
                            Triple("Analytics", Icons.Filled.PieChart, Icons.Outlined.PieChart)
                        )

                        navItems.forEachIndexed { index, (label, filledIcon, outlinedIcon) ->
                            val isSelected = uiState.currentTab == index
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setCurrentTab(index) },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) filledIcon else outlinedIcon,
                                        contentDescription = label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = FrostedIndigoLight,
                                    selectedTextColor = FrostedIndigoLight,
                                    indicatorColor = FrostedIndigoPill,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted
                                ),
                                modifier = Modifier.testTag("nav_item_$index")
                            )
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { viewModel.openAddTransactionSheet() },
                        containerColor = FrostedIndigo,
                        contentColor = Color.White,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .border(1.dp, Color(0x40FFFFFF), CircleShape)
                            .testTag("main_add_fab")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Transaction",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = uiState.currentTab,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> -width } + fadeOut()
                                )
                            } else {
                                (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> width } + fadeOut()
                                )
                            }
                        },
                        label = "tabContentTransition"
                    ) { targetTab ->
                        when (targetTab) {
                            0 -> HomeScreen(
                                uiState = uiState,
                                onPreviousMonth = { viewModel.previousMonth() },
                                onNextMonth = { viewModel.nextMonth() },
                                onMonthClick = { showMonthPicker = true },
                                onAddTransactionClick = { viewModel.openAddTransactionSheet() },
                                onSeeAllTransactionsClick = { viewModel.setCurrentTab(1) },
                                onViewReportsClick = { viewModel.setCurrentTab(2) },
                                onEditTransaction = { viewModel.openEditTransactionSheet(it) },
                                onDeleteTransaction = { viewModel.deleteTransaction(it) },
                                onOpenProfile = { viewModel.openProfileDialog() }
                            )
                            1 -> TransactionsScreen(
                                uiState = uiState,
                                onSearchChange = { viewModel.setSearchQuery(it) },
                                onFilterTypeChange = { viewModel.setFilterType(it) },
                                onCategoryFilterChange = { viewModel.setCategoryFilter(it) },
                                onEditTransaction = { viewModel.openEditTransactionSheet(it) },
                                onDeleteTransaction = { viewModel.deleteTransaction(it) }
                            )
                            2 -> ReportsScreen(
                                uiState = uiState,
                                onPreviousMonth = { viewModel.previousMonth() },
                                onNextMonth = { viewModel.nextMonth() },
                                onMonthClick = { showMonthPicker = true }
                            )
                            3 -> CategoriesScreen(
                                uiState = uiState,
                                onCurrencyChange = { viewModel.setCurrency(it) },
                                onResetSampleData = { viewModel.resetToSampleData() },
                                onClearAllData = { viewModel.clearAllData() },
                                onOpenProfile = { viewModel.openProfileDialog() }
                            )
                        }
                    }
                }

                // Add/Edit Transaction Bottom Sheet
                if (uiState.isAddEditSheetOpen) {
                    AddEditTransactionSheet(
                        transactionToEdit = uiState.editingTransaction,
                        currencySymbol = uiState.currencySymbol,
                        onDismiss = { viewModel.closeAddEditSheet() },
                        onSave = { id, title, amount, type, category, timestamp, note, paymentMethod ->
                            viewModel.saveTransaction(id, title, amount, type, category, timestamp, note, paymentMethod)
                        },
                        onDelete = { viewModel.deleteTransaction(it) }
                    )
                }

                // Month Picker Dialog
                if (showMonthPicker) {
                    MonthYearPickerDialog(
                        currentYear = uiState.selectedYear,
                        currentMonth = uiState.selectedMonth,
                        onDismiss = { showMonthPicker = false },
                        onMonthSelected = { year, month ->
                            viewModel.selectMonth(year, month)
                        }
                    )
                }

                // User Profile & Account Dialog
                if (uiState.isProfileDialogOpen) {
                    UserProfileDialog(
                        uiState = uiState,
                        onDismiss = { viewModel.closeProfileDialog() },
                        onLogout = { viewModel.logout() },
                        onSwitchUser = { viewModel.switchUser(it) },
                        onUpdateProfile = { name, budget, curr ->
                            viewModel.updateUserProfile(name, budget, curr)
                        }
                    )
                }
            }
        }
    }
}

