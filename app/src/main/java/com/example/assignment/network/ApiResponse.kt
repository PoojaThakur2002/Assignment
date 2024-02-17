package com.example.assignment.network

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()

    companion object {
        fun <T> success(data: T): ApiResponse<T> = Success(data)
        fun error(message: String): ApiResponse<Nothing> = Error(message)
        fun loading(): ApiResponse<Nothing> = Loading
    }
}