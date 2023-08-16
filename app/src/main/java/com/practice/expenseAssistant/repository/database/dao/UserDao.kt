package com.practice.expenseAssistant.repository.database.dao

import androidx.room.*
import com.practice.expenseAssistant.repository.database.entities.User

@Dao
interface UserDao {

    @Insert
    suspend fun setUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * from user WHERE name = :name AND password = :password")
    suspend fun getUser(name: String, password: String): User?
}