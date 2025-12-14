package com.example.Peerly.data.model


data class UserCreateRequest(
    val email: String,
    val fullName: String,
    val passwordHash: String,
    val avatarUrl: String? = null,
    val language: String? = "pt",
    val role: String? = null // "student" | "tutor" | "both" | "admin"
)
