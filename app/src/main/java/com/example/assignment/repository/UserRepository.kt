package com.example.assignment.repository

import com.example.assignment.network.ApiService
import com.example.assignment.network.ApiResponse
import com.example.assignment.ui.models.UserResponse
import java.lang.Exception

class UserRepository(private val apiService: ApiService) {

    suspend fun getUserData(page: Int): ApiResponse<UserResponse> {
        return try {
            val response = apiService.getUsersList(page)
            if (response.isSuccessful) {
                ApiResponse.success(response.body() ?: UserResponse())
            } else {
                ApiResponse.error(response.message())
            }
        } catch (e:Exception){
            ApiResponse.error(e.message ?: "unKnown Error")
        }


    }
}