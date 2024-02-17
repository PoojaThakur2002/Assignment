package com.example.assignment.network

import com.example.assignment.ui.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsersList(@Query("page")page:Int):Response<UserResponse>
}