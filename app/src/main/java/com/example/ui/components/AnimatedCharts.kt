package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FrostedEmeraldLight
import com.example.ui.theme.FrostedIndigoLight
import com.example.ui.theme.FrostedRoseLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassSurface
import com.example.ui.theme.TextDarkMuted
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.CategoryExpenseSummary
import com.example.ui.viewmodel.DailyTrendData
import kotlin.math.atan2

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimatedDonutChart(
    categories: List<CategoryExpenseSummary>,
    totalAmount: Double,
    currencySymbol: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    var selectedCategoryIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(categories) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("animated_donut_chart"),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .background(GlassSurface)
                .border(
                    1.dp,
                    GlassBorder,
                    RoundedCornerShape(28.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (categories.isEmpty() || totalAmount <= 0.0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No category data available",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                val selectedCategory = selectedCategoryIndex?.let { categories.getOrNull(it) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(190.dp)
                            .pointerInput(categories) {
                                detectTapGestures { offset ->
                                    val center = Offset(size.width / 2f, size.height / 2f)
                                    val dx = offset.x - center.x
                                    val dy = offset.y - center.y
                                    val distance = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
                                    val innerR = (size.width / 2f) * 0.55f
                                    val outerR = (size.width / 2f) * 0.95f

                                    if (distance in innerR..outerR) {
                                        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                        if (angle < 0) angle += 360f

                                        var accumulated = 0f
                                        categories.forEachIndexed { index, cat ->
                                            val sweep = (cat.percentage / 100f) * 360f
                                            val start = (accumulated - 90f + 360f) % 360f
                                            val end = (start + sweep) % 360f

                                            val isInSlice = if (start < end) {
                                                angle in start..end
                                            } else {
                                                angle >= start || angle <= end
                                            }

                                            if (isInSlice) {
                                                selectedCategoryIndex = if (selectedCategoryIndex == index) null else index
                                                return@detectTapGestures
                                            }
                                            accumulated += sweep
                                        }
                                    } else {
                                        selectedCategoryIndex = null
                                    }
                                }
                            }
                    ) {
                        val strokeWidth = 28.dp.toPx()
                        val diameter = size.minDimension - strokeWidth - 10.dp.toPx()
                        val arcSize = Size(diameter, diameter)
                        val arcTopLeft = Offset(
                            (size.width - diameter) / 2f,
                            (size.height - diameter) / 2f
                        )

                        var startAngle = -90f

                        // Background ring
                        drawArc(
                            color = Color(0x18FFFFFF),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = arcTopLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        categories.forEachIndexed { index, cat ->
                            val sweepAngle = (cat.percentage / 100f) * 360f * progress.value
                            val isSelected = selectedCategoryIndex == index

                            val currentStroke = if (isSelected) strokeWidth + 6.dp.toPx() else strokeWidth

                            drawArc(
                                color = cat.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle.coerceAtLeast(1.5f),
                                useCenter = false,
                                topLeft = arcTopLeft,
                                size = arcSize,
                                style = Stroke(width = currentStroke, cap = StrokeCap.Butt)
                            )
                            startAngle += sweepAngle
                        }
                    }

                    // Center text readout
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (selectedCategory != null) {
                            Text(
                                text = selectedCategory.categoryName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = selectedCategory.color,
                                maxLines = 1
                            )
                            Text(
                                text = CurrencyHelper.format(selectedCategory.totalAmount, currencySymbol),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite
                            )
                            Text(
                                text = String.format("%.1f%%", selectedCategory.percentage),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = selectedCategory.color
                            )
                        } else {
                            Text(
                                text = "TOTAL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = CurrencyHelper.format(totalAmount, currencySymbol),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite
                            )
                            Text(
                                text = "${categories.size} Categories",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Legend Chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEachIndexed { index, cat ->
                        val isSelected = selectedCategoryIndex == index
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) cat.color.copy(alpha = 0.25f) else Color(0x12FFFFFF))
                                .border(1.dp, if (isSelected) cat.color else Color(0x18FFFFFF), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(cat.color, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = cat.categoryName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) TextWhite else TextMuted
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format("%.0f%%", cat.percentage),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = cat.color
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedDailyTrendChart(
    trends: List<DailyTrendData>,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(trends) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("animated_daily_trend_chart"),
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .background(GlassSurface)
                .border(
                    1.dp,
                    GlassBorder,
                    RoundedCornerShape(28.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Activity Flow",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(FrostedEmeraldLight, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Income", fontSize = 11.sp, color = TextMuted)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(FrostedRoseLight, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Expense", fontSize = 11.sp, color = TextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val maxVal = remember(trends) {
                trends.maxOfOrNull { maxOf(it.incomeAmount, it.expenseAmount) }?.coerceAtLeast(100.0) ?: 100.0
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height - 20.dp.toPx()
                    val count = trends.size.coerceAtLeast(1)
                    val barGroupWidth = width / count
                    val singleBarWidth = (barGroupWidth * 0.35f).coerceAtLeast(4.dp.toPx())

                    // Draw grid lines
                    val steps = 3
                    for (i in 0..steps) {
                        val y = height * (i.toFloat() / steps)
                        drawLine(
                            color = Color(0x15FFFFFF),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    trends.forEachIndexed { i, item ->
                        val groupCenterX = (i * barGroupWidth) + (barGroupWidth / 2f)

                        // Income bar
                        val incomeHeight = ((item.incomeAmount / maxVal).toFloat() * height * progress.value).coerceAtLeast(0f)
                        val incomeX = groupCenterX - singleBarWidth - 2.dp.toPx()
                        drawRoundRect(
                            color = FrostedEmeraldLight,
                            topLeft = Offset(incomeX, height - incomeHeight),
                            size = Size(singleBarWidth, incomeHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                        )

                        // Expense bar
                        val expenseHeight = ((item.expenseAmount / maxVal).toFloat() * height * progress.value).coerceAtLeast(0f)
                        val expenseX = groupCenterX + 2.dp.toPx()
                        drawRoundRect(
                            color = FrostedRoseLight,
                            topLeft = Offset(expenseX, height - expenseHeight),
                            size = Size(singleBarWidth, expenseHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // X-axis Day labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                trends.forEachIndexed { i, item ->
                    if (trends.size <= 10 || i % (trends.size / 6).coerceAtLeast(1) == 0 || i == trends.lastIndex) {
                        Text(
                            text = "d${item.dayOfMonth}",
                            fontSize = 10.sp,
                            color = TextDarkMuted
                        )
                    }
                }
            }
        }
    }
}
