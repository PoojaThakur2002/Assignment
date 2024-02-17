package com.example.assignment.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.network.ApiResponse
import com.example.assignment.repository.UserRepository
import com.example.assignment.ui.models.UserDataItem
import com.example.assignment.ui.models.UserResponse
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val userMutableLiveData = MutableLiveData<ApiResponse<UserResponse>>()
    val userData: LiveData<ApiResponse<UserResponse>> = userMutableLiveData
    private var currentPage = 1
    fun getUsers() {
        viewModelScope.launch {
            userMutableLiveData.value = ApiResponse.loading()
            val response = userRepository.getUserData(currentPage)
            if (response is ApiResponse.Success) {
                currentPage ++
                userMutableLiveData.value = response
            } else if (response is ApiResponse.Error) {
                userMutableLiveData.value = response
            }
        }
    }
}