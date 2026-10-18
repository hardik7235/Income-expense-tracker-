package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Transaction
import com.example.data.model.TransactionType
import com.example.ui.components.CurrencyHelper
import com.example.ui.components.TransactionItemCard
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
fun TransactionsScreen(
    uiState: FinanceUiState,
    onSearchChange: (String) -> Unit,
    onFilterTypeChange: (TransactionType?) -> Unit,
    onCategoryFilterChange: (String?) -> Unit,
    onEditTransaction: (Transaction) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Frosted Search bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search by title, category, note...", color = TextMuted) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMuted
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextMuted
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GlassSurface,
                unfocusedContainerColor = GlassSurface,
                focusedBorderColor = FrostedIndigoLight,
                unfocusedBorderColor = GlassBorder,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("transactions_search_bar")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Type Filter Chips (All, Income, Expense)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ALL chip
            val isAll = uiState.filterType == null
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isAll) FrostedIndigoPill else GlassSurface)
                    .border(1.dp, if (isAll) FrostedIndigoLight else GlassBorder, RoundedCornerShape(14.dp))
                    .clickable { onFilterTypeChange(null) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("filter_chip_all")
            ) {
                Text(
                    text = "All (${uiState.transactions.size})",
                    fontSize = 12.sp,
                    fontWeight = if (isAll) FontWeight.Bold else FontWeight.Medium,
                    color = if (isAll) TextWhite else TextMuted
                )
            }

            // INCOME chip
            val isIncome = uiState.filterType == TransactionType.INCOME
            val incomeCount = uiState.transactions.count { it.type == TransactionType.INCOME }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isIncome) FrostedEmeraldGlassBg else GlassSurface)
                    .border(1.dp, if (isIncome) FrostedEmeraldGlassBorder else GlassBorder, RoundedCornerShape(14.dp))
                    .clickable { onFilterTypeChange(TransactionType.INCOME) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("filter_chip_income")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = FrostedEmeraldLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Income ($incomeCount)",
                        fontSize = 12.sp,
                        fontWeight = if (isIncome) FontWeight.Bold else FontWeight.Medium,
                        color = if (isIncome) FrostedEmeraldLight else TextMuted
                    )
                }
            }

            // EXPENSE chip
            val isExpense = uiState.filterType == TransactionType.EXPENSE
            val expenseCount = uiState.transactions.count { it.type == TransactionType.EXPENSE }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isExpense) FrostedRoseGlassBg else GlassSurface)
                    .border(1.dp, if (isExpense) FrostedRoseGlassBorder else GlassBorder, RoundedCornerShape(14.dp))
                    .clickable { onFilterTypeChange(TransactionType.EXPENSE) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("filter_chip_expense")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = FrostedRoseLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Expense ($expenseCount)",
                        fontSize = 12.sp,
                        fontWeight = if (isExpense) FontWeight.Bold else FontWeight.Medium,
                        color = if (isExpense) FrostedRoseLight else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Category Filter Carousel
        val allCategories = uiState.transactions.map { it.category }.distinct()
        if (allCategories.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val isAllCats = uiState.selectedCategoryFilter == null
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isAllCats) FrostedIndigoPill else Color(0x12FFFFFF))
                            .border(1.dp, if (isAllCats) FrostedIndigoLight else Color(0x18FFFFFF), RoundedCornerShape(12.dp))
                            .clickable { onCategoryFilterChange(null) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "All Categories",
                            fontSize = 11.sp,
                            fontWeight = if (isAllCats) FontWeight.Bold else FontWeight.Normal,
                            color = if (isAllCats) TextWhite else TextMuted
                        )
                    }
                }

                items(allCategories) { catName ->
                    val isSelected = uiState.selectedCategoryFilter == catName
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) FrostedIndigoPill else Color(0x12FFFFFF))
                            .border(1.dp, if (isSelected) FrostedIndigoLight else Color(0x18FFFFFF), RoundedCornerShape(12.dp))
                            .clickable {
                                onCategoryFilterChange(if (isSelected) null else catName)
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = catName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TextWhite else TextMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filtered count & net header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${uiState.filteredTransactions.size} Transactions Found",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )

            val totalFiltered = uiState.filteredTransactions.sumOf {
                if (it.type == TransactionType.INCOME) it.amount else -it.amount
            }
            Text(
                text = "Net: ${CurrencyHelper.format(totalFiltered, uiState.currencySymbol)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (totalFiltered >= 0) FrostedEmeraldLight else FrostedRoseLight
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Transaction List
        if (uiState.filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = TextDarkMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No matching transactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try adjusting your search query or filters",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(
                    items = uiState.filteredTransactions,
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
        }
    }
}
