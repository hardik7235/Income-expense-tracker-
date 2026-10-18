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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryItem
import com.example.data.model.TransactionType
import com.example.ui.components.CurrencyHelper
import com.example.ui.theme.FrostedEmerald
import com.example.ui.theme.FrostedEmeraldGlassBg
import com.example.ui.theme.FrostedEmeraldGlassBorder
import com.example.ui.theme.FrostedEmeraldLight
import com.example.ui.theme.FrostedIndigo
import com.example.ui.theme.FrostedIndigoLight
import com.example.ui.theme.FrostedIndigoPill
import com.example.ui.theme.FrostedRose
import com.example.ui.theme.FrostedRoseGlassBg
import com.example.ui.theme.FrostedRoseGlassBorder
import com.example.ui.theme.FrostedRoseLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.FinanceUiState

@Composable
fun CategoriesScreen(
    uiState: FinanceUiState,
    onCurrencyChange: (String) -> Unit,
    onResetSampleData: () -> Unit,
    onClearAllData: () -> Unit,
    onOpenProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showClearDataConfirm by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val categoriesSummary = if (selectedType == TransactionType.EXPENSE)
        uiState.expenseCategories
    else
        uiState.incomeCategories

    val totalTypeAmount = if (selectedType == TransactionType.EXPENSE)
        uiState.totalExpense
    else
        uiState.totalIncome

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Account Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(26.dp))
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(FrostedIndigo, CircleShape)
                                .border(1.dp, FrostedIndigoLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.currentUser?.initials ?: "U",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = uiState.currentUser?.name ?: "User Profile",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextWhite
                            )
                            Text(
                                text = uiState.currentUser?.email ?: "ayanokojikiyotaka7235@gmail.com",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(FrostedIndigoPill)
                            .border(1.dp, FrostedIndigoLight, RoundedCornerShape(14.dp))
                            .clickable { onOpenProfile() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("manage_account_button")
                    ) {
                        Text("Manage", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = FrostedIndigoLight)
                    }
                }
            }
        }
        // App Preferences & Currency Selector Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(26.dp))
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(FrostedIndigoPill, CircleShape)
                                .border(1.dp, FrostedIndigoLight.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = FrostedIndigoLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Active Currency",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextWhite
                            )
                            Text(
                                text = CurrencyHelper.AVAILABLE_CURRENCIES.find { it.first == uiState.currencySymbol }?.second ?: uiState.currencySymbol,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(FrostedIndigoPill)
                            .border(1.dp, FrostedIndigoLight, RoundedCornerShape(14.dp))
                            .clickable { showCurrencyDialog = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("change_currency_button")
                    ) {
                        Text("Change", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = FrostedIndigoLight)
                    }
                }
            }
        }

        // Category Switcher Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(GlassSurface)
                    .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
                    .padding(4.dp)
            ) {
                // Expense tab
                val isExpense = selectedType == TransactionType.EXPENSE
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isExpense) FrostedRoseGlassBg else Color.Transparent)
                        .border(1.dp, if (isExpense) FrostedRoseGlassBorder else Color.Transparent, RoundedCornerShape(14.dp))
                        .clickable { selectedType = TransactionType.EXPENSE }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Expense Analytics",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isExpense) FrostedRoseLight else TextMuted
                    )
                }

                // Income tab
                val isIncome = selectedType == TransactionType.INCOME
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isIncome) FrostedEmeraldGlassBg else Color.Transparent)
                        .border(1.dp, if (isIncome) FrostedEmeraldGlassBorder else Color.Transparent, RoundedCornerShape(14.dp))
                        .clickable { selectedType = TransactionType.INCOME }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Income Analytics",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isIncome) FrostedEmeraldLight else TextMuted
                    )
                }
            }
        }

        // Total Amount for current category type
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .background(if (selectedType == TransactionType.INCOME) FrostedEmeraldGlassBg else FrostedRoseGlassBg)
                        .border(
                            1.dp,
                            if (selectedType == TransactionType.INCOME) FrostedEmeraldGlassBorder else FrostedRoseGlassBorder,
                            RoundedCornerShape(22.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (selectedType == TransactionType.INCOME) "TOTAL MONTHLY INCOME" else "TOTAL MONTHLY EXPENSES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = CurrencyHelper.format(totalTypeAmount, uiState.currencySymbol),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == TransactionType.INCOME) FrostedEmeraldLight else FrostedRoseLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22FFFFFF))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${categoriesSummary.size} Categories",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == TransactionType.INCOME) FrostedEmeraldLight else FrostedRoseLight
                        )
                    }
                }
            }
        }

        // Category Items with progress bars
        if (categoriesSummary.isEmpty()) {
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
                            imageVector = Icons.Default.Category,
                            contentDescription = null,
                            tint = TextDarkMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No category data for ${uiState.selectedMonthLabel}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextWhite
                        )
                    }
                }
            }
        } else {
            items(categoriesSummary) { cat ->
                val categoryObj = CategoryItem.getCategoryByName(cat.categoryName, selectedType)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Transparent
                ) {
                    Column(
                        modifier = Modifier
                            .background(GlassSurface)
                            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(cat.color.copy(alpha = 0.18f))
                                    .border(1.dp, cat.color.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryObj.icon,
                                    contentDescription = null,
                                    tint = cat.color,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cat.categoryName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "${cat.transactionCount} transactions",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = CurrencyHelper.format(cat.totalAmount, uiState.currencySymbol),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = String.format("%.1f%%", cat.percentage),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = cat.color
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { (cat.percentage / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = cat.color,
                            trackColor = Color(0x22FFFFFF)
                        )
                    }
                }
            }
        }

        // Data Management Controls
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(26.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(26.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Data Management",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x18FFFFFF))
                                .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(14.dp))
                                .clickable { showResetConfirm = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reset Sample", fontSize = 12.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(FrostedRoseGlassBg)
                                .border(1.dp, FrostedRoseGlassBorder, RoundedCornerShape(14.dp))
                                .clickable { showClearDataConfirm = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = null,
                                    tint = FrostedRoseLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Clear All", fontSize = 12.sp, color = FrostedRoseLight, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    // Currency Selection Dialog
    if (showCurrencyDialog) {
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            containerColor = Color(0xFF131722),
            title = { Text("Select Preferred Currency", fontWeight = FontWeight.Bold, color = TextWhite) },
            text = {
                Column {
                    CurrencyHelper.AVAILABLE_CURRENCIES.forEach { (symbol, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (uiState.currencySymbol == symbol) FrostedIndigoPill else Color.Transparent)
                                .clickable {
                                    onCurrencyChange(symbol)
                                    showCurrencyDialog = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name,
                                fontSize = 14.sp,
                                fontWeight = if (uiState.currencySymbol == symbol) FontWeight.Bold else FontWeight.Normal,
                                color = if (uiState.currencySymbol == symbol) FrostedIndigoLight else TextWhite
                            )
                            if (uiState.currencySymbol == symbol) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = FrostedIndigoLight,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCurrencyDialog = false }) {
                    Text("Close", color = FrostedIndigoLight)
                }
            }
        )
    }

    // Clear Data Confirmation Dialog
    if (showClearDataConfirm) {
        AlertDialog(
            onDismissRequest = { showClearDataConfirm = false },
            containerColor = Color(0xFF131722),
            title = { Text("Clear All Transactions?", fontWeight = FontWeight.Bold, color = TextWhite) },
            text = { Text("This will permanently delete all stored income and expense records from the database.", color = TextMuted) },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllData()
                        showClearDataConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedRose)
                ) {
                    Text("Clear All", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataConfirm = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    // Reset Sample Data Confirmation Dialog
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            containerColor = Color(0xFF131722),
            title = { Text("Reset to Sample Data?", fontWeight = FontWeight.Bold, color = TextWhite) },
            text = { Text("This will replace current transactions with realistic pre-populated monthly demo entries.", color = TextMuted) },
            confirmButton = {
                Button(
                    onClick = {
                        onResetSampleData()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FrostedIndigo)
                ) {
                    Text("Reset", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}
