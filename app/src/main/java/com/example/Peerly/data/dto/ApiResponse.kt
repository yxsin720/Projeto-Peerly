package com.example.Peerly.data.dto

data class ApiResponse<T>(
    val message: String? = null,
    val data: T? = null
)
