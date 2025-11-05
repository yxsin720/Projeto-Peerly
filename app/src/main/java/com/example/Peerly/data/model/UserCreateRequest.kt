package com.example.Peerly.data.model

/**
 * DTO para o endpoint POST /api/users (UserController).
 * IMPORTANTE: o campo da password no backend chama-se "passwordHash".
 */
data class UserCreateRequest(
    val email: String,
    val fullName: String,
    val passwordHash: String,
    val avatarUrl: String? = null,
    val language: String? = "pt",
    val role: String? = null // "student" | "tutor" | "both" | "admin"
)
