package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CategoryItem
import com.example.data.model.Transaction
import com.example.data.model.TransactionType
import com.example.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(entities = [Transaction::class, User::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateInitialData(database.userDao(), database.transactionDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(userDao: UserDao, transactionDao: TransactionDao) {
            val defaultUser = User(
                id = 1L,
                name = "Kiyotaka Ayanokoji",
                email = "ayanokojikiyotaka7235@gmail.com",
                passwordHash = "password123",
                avatarIcon = "person",
                avatarBgColor = 0xFF6366F1,
                currencySymbol = "₹",
                monthlyBudget = 50000.0,
                createdAt = System.currentTimeMillis() - (60L * 24 * 60 * 60 * 1000)
            )

            if (userDao.countUsers() == 0) {
                userDao.insertUser(defaultUser)
            }

            if (transactionDao.getCount() > 0) return

            val cal = Calendar.getInstance()
            val now = cal.timeInMillis

            fun getPastTimestamp(daysAgo: Int, hour: Int, minute: Int): Long {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, -daysAgo)
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)
                return c.timeInMillis
            }

            val sampleTransactions = listOf(
                Transaction(
                    userId = 1L,
                    title = "Monthly Salary",
                    amount = 75000.00,
                    type = TransactionType.INCOME,
                    category = "Salary",
                    categoryIcon = "work",
                    categoryColor = 0xFF10B981,
                    timestamp = getPastTimestamp(15, 9, 30),
                    note = "Direct payroll deposit into HDFC account",
                    paymentMethod = "Bank Transfer"
                ),
                Transaction(
                    userId = 1L,
                    title = "Apartment Rent",
                    amount = 18500.00,
                    type = TransactionType.EXPENSE,
                    category = "Housing & Rent",
                    categoryIcon = "home",
                    categoryColor = 0xFF8B5CF6,
                    timestamp = getPastTimestamp(14, 11, 0),
                    note = "Monthly flat rental payment",
                    paymentMethod = "UPI"
                ),
                Transaction(
                    userId = 1L,
                    title = "Freelance Mobile UI Project",
                    amount = 32000.00,
                    type = TransactionType.INCOME,
                    category = "Freelance",
                    categoryIcon = "computer",
                    categoryColor = 0xFF06B6D4,
                    timestamp = getPastTimestamp(10, 15, 45),
                    note = "Android Kotlin app design milestone",
                    paymentMethod = "UPI"
                ),
                Transaction(
                    userId = 1L,
                    title = "Blinkit & Groceries",
                    amount = 4250.00,
                    type = TransactionType.EXPENSE,
                    category = "Groceries",
                    categoryIcon = "shopping_cart",
                    categoryColor = 0xFFF97316,
                    timestamp = getPastTimestamp(8, 17, 20),
                    note = "Pantry staples, dairy & fruits",
                    paymentMethod = "UPI"
                ),
                Transaction(
                    userId = 1L,
                    title = "Dinner at Barbeque Nation",
                    amount = 1850.00,
                    type = TransactionType.EXPENSE,
                    category = "Food & Dining",
                    categoryIcon = "restaurant",
                    categoryColor = 0xFFF43F5E,
                    timestamp = getPastTimestamp(6, 20, 15),
                    note = "Weekend dinner with team",
                    paymentMethod = "Credit Card"
                ),
                Transaction(
                    userId = 1L,
                    title = "SIP Mutual Fund Dividend",
                    amount = 4500.00,
                    type = TransactionType.INCOME,
                    category = "Investments",
                    categoryIcon = "trending_up",
                    categoryColor = 0xFF8B5CF6,
                    timestamp = getPastTimestamp(5, 10, 0),
                    note = "Quarterly Nifty 50 index fund payout",
                    paymentMethod = "Bank Transfer"
                ),
                Transaction(
                    userId = 1L,
                    title = "Electricity & WiFi Bill",
                    amount = 2450.00,
                    type = TransactionType.EXPENSE,
                    category = "Utilities & Bills",
                    categoryIcon = "bolt",
                    categoryColor = 0xFFEAB308,
                    timestamp = getPastTimestamp(4, 14, 10),
                    note = "TNEB Power bill & Airtel Xstream",
                    paymentMethod = "UPI"
                ),
                Transaction(
                    userId = 1L,
                    title = "Ola & Uber Rides",
                    amount = 780.00,
                    type = TransactionType.EXPENSE,
                    category = "Transportation",
                    categoryIcon = "directions_car",
                    categoryColor = 0xFF3B82F6,
                    timestamp = getPastTimestamp(3, 18, 40),
                    note = "Office commute & airport drop",
                    paymentMethod = "Digital Wallet"
                ),
                Transaction(
                    userId = 1L,
                    title = "Netflix & Hotstar Subs",
                    amount = 699.00,
                    type = TransactionType.EXPENSE,
                    category = "Subscriptions",
                    categoryIcon = "subscriptions",
                    categoryColor = 0xFFD946EF,
                    timestamp = getPastTimestamp(2, 8, 0),
                    note = "Monthly premium family plans",
                    paymentMethod = "Debit Card"
                ),
                Transaction(
                    userId = 1L,
                    title = "Amazon Shopping",
                    amount = 3200.00,
                    type = TransactionType.EXPENSE,
                    category = "Shopping",
                    categoryIcon = "shopping_bag",
                    categoryColor = 0xFFEC4899,
                    timestamp = getPastTimestamp(1, 16, 30),
                    note = "Ergonomic keyboard wrist rest",
                    paymentMethod = "Credit Card"
                ),
                Transaction(
                    userId = 1L,
                    title = "Third Wave Coffee & Snacks",
                    amount = 340.00,
                    type = TransactionType.EXPENSE,
                    category = "Food & Dining",
                    categoryIcon = "restaurant",
                    categoryColor = 0xFFF43F5E,
                    timestamp = now,
                    note = "Iced latte & croissant",
                    paymentMethod = "UPI"
                )
            )

            transactionDao.insertAll(sampleTransactions)
        }
    }
}
