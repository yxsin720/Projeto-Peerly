package com.example.Peerly.data.model

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "id")        val id: String?,
    @Json(name = "email")     val email: String?,
    @Json(name = "fullName")  val fullName: String?,
    @Json(name = "role")      val role: String?,
    @Json(name = "avatarUrl") val avatarUrl: String?,
    @Json(name = "area")      val area: String?
)

fun UserResponse.toDomain(): User = User(
    id        = id.orEmpty(),
    email     = email.orEmpty(),
    fullName  = fullName.orEmpty(),
    role      = role.orEmpty(),
    avatarUrl = avatarUrl.orEmpty(),
    area      = area.orEmpty()
)
