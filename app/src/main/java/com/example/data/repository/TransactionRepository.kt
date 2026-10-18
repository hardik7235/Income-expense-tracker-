package com.example.data.repository

import com.example.data.local.TransactionDao
import com.example.data.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {

    fun getTransactionsForUser(userId: Long): Flow<List<Transaction>> {
        return dao.getTransactionsForUser(userId)
    }

    val allTransactions: Flow<List<Transaction>> = dao.getAllTransactions()

    fun getTransactionsBetweenForUser(userId: Long, startTime: Long, endTime: Long): Flow<List<Transaction>> {
        return dao.getTransactionsBetweenForUser(userId, startTime, endTime)
    }

    fun getTransactionsBetween(startTime: Long, endTime: Long): Flow<List<Transaction>> {
        return dao.getTransactionsBetween(startTime, endTime)
    }

    suspend fun getTransactionById(id: Long): Transaction? {
        return dao.getTransactionById(id)
    }

    suspend fun insert(transaction: Transaction): Long {
        return dao.insertTransaction(transaction)
    }

    suspend fun insertAll(transactions: List<Transaction>) {
        dao.insertAll(transactions)
    }

    suspend fun update(transaction: Transaction) {
        dao.updateTransaction(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }

    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearAllForUser(userId: Long) {
        dao.deleteAllForUser(userId)
    }

    suspend fun clearAll() {
        dao.deleteAll()
    }
}
