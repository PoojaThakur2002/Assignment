package com.example.assignment.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.assignment.ui.models.UserDataItem

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavUser(user: UserDataItem)

    @Query("SELECT * FROM user")
    suspend fun getFavUsers() :List<UserDataItem>

    @Delete
    suspend fun deleteUser(user: UserDataItem)
}