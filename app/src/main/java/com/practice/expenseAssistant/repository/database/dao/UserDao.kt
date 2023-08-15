package com.practice.expenseAssistant.repository.database.dao

import androidx.room.*
import com.practice.expenseAssistant.repository.database.entities.User

@Dao
interface UserDao {

    @Insert
    fun setUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * from user WHERE name = :name AND password = :password")
    fun getUser(name: String, password: String): User?
}