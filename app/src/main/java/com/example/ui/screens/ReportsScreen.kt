package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedDailyTrendChart
import com.example.ui.components.AnimatedDonutChart
import com.example.ui.components.CurrencyHelper
import com.example.ui.theme.FrostedEmerald
import com.example.ui.theme.FrostedEmeraldLight
import com.example.ui.theme.FrostedIndigo
import com.example.ui.theme.FrostedIndigoLight
import com.example.ui.theme.FrostedRose
import com.example.ui.theme.FrostedRoseLight
import com.example.ui.theme.FrostedTeal
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.FinanceUiState

@Composable
fun ReportsScreen(
    uiState: FinanceUiState,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val healthGrade = when {
        uiState.savingsRate >= 40f -> "Excellent"
        uiState.savingsRate >= 20f -> "Healthy"
        uiState.savingsRate > 0f -> "Fair"
        else -> "Deficit"
    }

    val healthColor = when {
        uiState.savingsRate >= 40f -> FrostedTeal
        uiState.savingsRate >= 20f -> FrostedEmeraldLight
        uiState.savingsRate > 0f -> Color(0xFFFBBF24)
        else -> FrostedRoseLight
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Frosted Month Selector Bar
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_month_selector"),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onPreviousMonth,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x18FFFFFF), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month",
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onMonthClick() }
                    ) {
                        Text(
                            text = "MONTHLY FINANCIAL REPORT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrostedIndigoLight,
                            letterSpacing = 1.4.sp
                        )
                        Text(
                            text = uiState.selectedMonthLabel,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    IconButton(
                        onClick = onNextMonth,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x18FFFFFF), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Month",
                            tint = TextWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Frosted Glass Financial Health Card
        item {
            val cardGradient = Brush.linearGradient(
                colors = listOf(
                    Color(0x354F46E5),
                    Color(0x20312E81),
                    Color(0x250F172A)
                )
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .background(cardGradient)
                        .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color(0x26FFFFFF), CircleShape)
                                        .border(1.dp, Color(0x33FFFFFF), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = FrostedTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "FINANCIAL HEALTH",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = healthGrade,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = healthColor
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(healthColor.copy(alpha = 0.2f))
                                    .border(1.dp, healthColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${String.format("%.1f", uiState.savingsRate)}% Saved",
                                    color = healthColor,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Net Monthly Summary Numbers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Total Income block
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x18FFFFFF))
                                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(16.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Total Income",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = CurrencyHelper.format(uiState.totalIncome, uiState.currencySymbol),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FrostedEmeraldLight
                                )
                            }

                            // Total Expense block
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x18FFFFFF))
                                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(16.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Total Expense",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = CurrencyHelper.format(uiState.totalExpense, uiState.currencySymbol),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FrostedRoseLight
                                )
                            }

                            // Net Surplus
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x18FFFFFF))
                                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(16.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Net Balance",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = CurrencyHelper.format(uiState.netBalance, uiState.currencySymbol),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.netBalance >= 0) FrostedEmeraldLight else FrostedRoseLight
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Average Metrics Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Monthly Spending Metrics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Daily Average Burn Rate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x12FFFFFF))
                                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Daily Burn Rate",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = CurrencyHelper.format(uiState.dailyAverageExpense, uiState.currencySymbol),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Avg. per day",
                                    fontSize = 10.sp,
                                    color = TextDarkMuted
                                )
                            }
                        }

                        // Total Transactions Count
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0x12FFFFFF))
                                .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Activity Volume",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${uiState.transactions.size} Entries",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Recorded this month",
                                    fontSize = 10.sp,
                                    color = TextDarkMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Activity Trend Chart
        if (uiState.dailyTrends.isNotEmpty()) {
            item {
                AnimatedDailyTrendChart(
                    trends = uiState.dailyTrends,
                    currencySymbol = uiState.currencySymbol
                )
            }
        }

        // Expense Category Breakdown with Donut Chart
        item {
            AnimatedDonutChart(
                categories = uiState.expenseCategories,
                totalAmount = uiState.totalExpense,
                currencySymbol = uiState.currencySymbol,
                title = "Expense Category Breakdown"
            )
        }

        // Income Category Breakdown
        if (uiState.incomeCategories.isNotEmpty()) {
            item {
                AnimatedDonutChart(
                    categories = uiState.incomeCategories,
                    totalAmount = uiState.totalIncome,
                    currencySymbol = uiState.currencySymbol,
                    title = "Income Sources Breakdown"
                )
            }
        }

        // Payment Method Distribution Card
        if (uiState.paymentBreakdown.isNotEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier
                            .background(GlassSurface)
                            .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Payment Method Distribution",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            uiState.paymentBreakdown.forEach { item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = item.method,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextWhite
                                        )
                                        Text(
                                            text = "${CurrencyHelper.format(item.amount, uiState.currencySymbol)} (${String.format("%.1f", item.percentage)}%)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextMuted
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { item.percentage / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = FrostedIndigoLight,
                                        trackColor = Color(0x22FFFFFF)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Frosted Insights Tip Card
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
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Monthly Financial Insights",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val topExpense = uiState.expenseCategories.firstOrNull()
                    if (topExpense != null) {
                        Text(
                            text = "• Largest spending sector is '${topExpense.categoryName}' accounting for ${String.format("%.1f", topExpense.percentage)}% of your monthly outflows.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    if (uiState.savingsRate >= 20f) {
                        Text(
                            text = "• Great work! You are saving ${String.format("%.1f", uiState.savingsRate)}% of your income, comfortably meeting target financial security thresholds.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    } else if (uiState.totalExpense > uiState.totalIncome && uiState.totalIncome > 0) {
                        Text(
                            text = "• Monthly expenses exceed income by ${CurrencyHelper.format(uiState.totalExpense - uiState.totalIncome, uiState.currencySymbol)}. Review discretionary expenses to restore balance.",
                            fontSize = 12.sp,
                            color = FrostedRoseLight
                        )
                    } else {
                        Text(
                            text = "• Try aiming for a 20%+ savings buffer each month to build emergency reserves and long-term wealth.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
