package com.example.Peerly.data.model

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "id")        val id: String?,
    @Json(name = "email")     val email: String?,
    @Json(name = "fullName")  val fullName: String?,
    @Json(name = "role")      val role: String?,
    @Json(name = "avatarUrl") val avatarUrl: String? = null   // <-- adiciona (opcional)
)

// Mapeia para o modelo de domínio usado no app
fun UserResponse.toDomain(): User = User(
    id = id,
    email = email,
    fullName = fullName,
    role = role,
    avatarUrl = avatarUrl     // <-- passa adiante
)
