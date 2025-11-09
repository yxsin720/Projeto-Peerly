package com.example.Peerly.data

import com.example.Peerly.data.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AuthRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    suspend fun login(email: String, password: String): User {
        val res = api.login(LoginRequest(email, password))
        return res.toDomain()
    }

    suspend fun registerViaUsers(
        fullName: String,
        email: String,
        password: String,
        area: String? = null
    ): User {
        val req = UserCreateRequest(
            email = email,
            fullName = fullName,
            passwordHash = password,
            language = "pt",
            role = null
        )
        val created = api.createUser(req)
        val a = area?.takeIf { it.isNotBlank() }
        return if (a != null && !created.id.isNullOrBlank()) {
            api.updateUser(created.id!!, mapOf("area" to a)).toDomain()
        } else created.toDomain()
    }

    suspend fun emailExists(email: String): Boolean {
        val users = api.getUsers()
        return users.any { it.email.equals(email, ignoreCase = true) }
    }

    suspend fun uploadUserAvatar(userId: String, file: File): String? {
        if (userId.isBlank()) return null
        val body = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val userResponse = api.uploadUserAvatar(userId, part)
        return userResponse.avatarUrl
    }

    suspend fun uploadTutorAvatar(tutorId: String, file: File): String {
        val body = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val map = api.uploadTutorAvatar(tutorId, part)
        return map["avatarUrl"].orEmpty()
    }

    suspend fun updateArea(userId: String, area: String): User =
        api.updateUser(userId, mapOf("area" to area)).toDomain()
}
