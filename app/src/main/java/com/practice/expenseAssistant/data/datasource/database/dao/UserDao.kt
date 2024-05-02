package com.practice.expenseAssistant.data.datasource.database.dao

import androidx.room.*
import com.practice.expenseAssistant.data.datasource.database.entities.User

@Dao
interface UserDao {

    @Insert
    suspend fun setUser(user: User)

    @Update()
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * from user WHERE name = :name AND password = :password")
    suspend fun getUser(name: String, password: String): User?

    @Query("UPDATE user SET password = :newPassword WHERE name = :name AND password = :oldPassword")
    suspend fun updatePassword(name: String, oldPassword: String, newPassword: String)
}