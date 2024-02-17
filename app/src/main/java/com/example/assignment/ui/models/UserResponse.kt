package com.example.assignment.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey

class UserResponse : ArrayList<UserDataItem>()

@Entity(tableName = "user")
data class UserDataItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val email: String,
    val gender: String,
    val name: String,
    val status: String,
    var isFavorite: Boolean = false
)