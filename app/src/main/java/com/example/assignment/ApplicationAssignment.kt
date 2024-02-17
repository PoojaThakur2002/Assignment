package com.example.assignment

import android.app.Application
import com.example.assignment.network.ApiService
import com.example.assignment.network.RetrofitHelper
import com.example.assignment.repository.UserRepository

class ApplicationAssignment : Application() {
    lateinit var userRepository:UserRepository
    override fun onCreate() {
        super.onCreate()
        initialized()
    }

    private fun initialized() {
        val apiInterface = RetrofitHelper.getInstance().create(ApiService::class.java)
        userRepository = UserRepository(apiInterface)
    }
}