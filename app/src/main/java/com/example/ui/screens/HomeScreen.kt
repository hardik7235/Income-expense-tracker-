package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Transaction
import com.example.ui.components.AnimatedBalanceCard
import com.example.ui.components.AnimatedDailyTrendChart
import com.example.ui.components.AnimatedDonutChart
import com.example.ui.components.CurrencyHelper
import com.example.ui.components.TransactionItemCard
import com.example.ui.theme.FrostedButtonGradient
import com.example.ui.theme.FrostedEmerald
import com.example.ui.theme.FrostedEmeraldLight
import com.example.ui.theme.FrostedIndigo
import com.example.ui.theme.FrostedIndigoLight
import com.example.ui.theme.FrostedIndigoPill
import com.example.ui.theme.FrostedRose
import com.example.ui.theme.FrostedRoseGlassBg
import com.example.ui.theme.FrostedRoseGlassBorder
import com.example.ui.theme.FrostedRoseLight
import com.example.ui.theme.FrostedAmber
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.FinanceUiState

@Composable
fun HomeScreen(
    uiState: FinanceUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthClick: () -> Unit,
    onAddTransactionClick: () -> Unit,
    onSeeAllTransactionsClick: () -> Unit,
    onViewReportsClick: () -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    onOpenProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Frosted Glass Header with Title & Avatar Pill
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(FrostedEmeraldLight, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "REAL-TIME DATABASE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrostedEmeraldLight,
                            letterSpacing = 1.4.sp
                        )
                    }
                    Text(
                        text = "Hello, ${uiState.currentUser?.name?.split(" ")?.firstOrNull() ?: "User"}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }

                // Avatar Pill (Click to open profile)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(FrostedIndigoPill)
                        .border(1.dp, FrostedIndigoLight.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { onOpenProfile() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("home_profile_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = getAvatarVector(uiState.currentUser?.avatarIcon),
                            contentDescription = "User Profile",
                            tint = FrostedIndigoLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = uiState.currentUser?.initials ?: "U",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }

        // Hero Frosted Glass Balance Card
        item {
            AnimatedBalanceCard(
                monthLabel = uiState.selectedMonthLabel,
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                netBalance = uiState.netBalance,
                savingsRate = uiState.savingsRate,
                currencySymbol = uiState.currencySymbol,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth,
                onMonthClick = onMonthClick
            )
        }

        // Monthly Budget Tracker Card
        item {
            val budget = uiState.monthlyBudget
            val spent = uiState.totalExpense
            val remaining = (budget - spent).coerceAtLeast(0.0)
            val usedPct = uiState.budgetUsedPercentage

            val progressColor = when {
                usedPct >= 95f -> FrostedRose
                usedPct >= 75f -> FrostedAmber
                else -> FrostedEmerald
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(22.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(progressColor.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, progressColor.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = progressColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Monthly Budget (INR)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "${String.format("%.0f", usedPct)}% of ${CurrencyHelper.formatCompact(budget, uiState.currencySymbol)} used",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${CurrencyHelper.formatCompact(remaining, uiState.currencySymbol)} left",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (remaining > 0) FrostedEmeraldLight else FrostedRoseLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (usedPct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = progressColor,
                        trackColor = Color(0x20FFFFFF)
                    )
                }
            }
        }

        // Frosted Glass Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // New Transaction Button with Indigo Gradient
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(FrostedButtonGradient)
                        .clickable { onAddTransactionClick() }
                        .testTag("quick_add_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "New Transaction",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                // View Reports Button with Frosted Glass Border
                Box(
                    modifier = Modifier
                        .weight(0.75f)
                        .height(50.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
                        .clickable { onViewReportsClick() }
                        .testTag("quick_reports_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = null,
                            tint = FrostedIndigoLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Reports",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextWhite
                        )
                    }
                }
            }
        }

        // Daily Activity Chart
        if (uiState.dailyTrends.isNotEmpty()) {
            item {
                AnimatedDailyTrendChart(
                    trends = uiState.dailyTrends,
                    currencySymbol = uiState.currencySymbol
                )
            }
        }

        // Category breakdown preview
        if (uiState.expenseCategories.isNotEmpty()) {
            item {
                AnimatedDonutChart(
                    categories = uiState.expenseCategories.take(5),
                    totalAmount = uiState.totalExpense,
                    currencySymbol = uiState.currencySymbol,
                    title = "Top Expenses Breakdown"
                )
            }
        }

        // Recent Activity Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                if (uiState.transactions.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSeeAllTransactionsClick() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View Report",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrostedIndigoLight
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = FrostedIndigoLight,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        if (uiState.transactions.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier
                            .background(GlassSurface)
                            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = TextMuted.copy(alpha = 0.6f),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No transactions in ${uiState.selectedMonthLabel}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap 'New Transaction' to record your first entry.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            items(
                items = uiState.transactions.take(5),
                key = { it.id }
            ) { tx ->
                TransactionItemCard(
                    transaction = tx,
                    currencySymbol = uiState.currencySymbol,
                    onEdit = onEditTransaction,
                    onDelete = onDeleteTransaction
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

