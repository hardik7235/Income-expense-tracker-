package com.example.data.repository

import com.example.data.local.UserDao
import com.example.data.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {

    fun getAllUsers(): Flow<List<User>> = dao.getAllUsers()

    fun getUserFlowById(id: Long): Flow<User?> = dao.getUserFlowById(id)

    suspend fun getUserByEmail(email: String): User? = dao.getUserByEmail(email)

    suspend fun getUserById(id: Long): User? = dao.getUserById(id)

    suspend fun insert(user: User): Long = dao.insertUser(user)

    suspend fun update(user: User) = dao.updateUser(user)

    suspend fun countUsers(): Int = dao.countUsers()
}
