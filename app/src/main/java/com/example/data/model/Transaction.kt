package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long = 1L,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val categoryIcon: String = "payments",
    val categoryColor: Long = 0xFF10B981,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = "",
    val paymentMethod: String = "UPI"
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

    val formattedTime: String
        get() {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

    val formattedDateTime: String
        get() = "$formattedDate • $formattedTime"
}
