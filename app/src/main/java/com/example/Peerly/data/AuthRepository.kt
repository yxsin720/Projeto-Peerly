package com.example.Peerly.data

import com.example.Peerly.data.model.LoginRequest
import com.example.Peerly.data.model.User
import com.example.Peerly.data.model.UserCreateRequest
import com.example.Peerly.data.model.toDomain
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

    suspend fun registerViaUsers(fullName: String, email: String, password: String): User {
        val req = UserCreateRequest(
            email = email,
            fullName = fullName,
            passwordHash = password,
            language = "pt",
            role = null
        )
        val res = api.createUser(req)
        return res.toDomain()
    }

    suspend fun emailExists(email: String): Boolean {
        val users = api.getUsers()
        return users.any { it.email.equals(email, ignoreCase = true) }
    }


    suspend fun uploadUserAvatar(userId: String, file: File): String? {
        if (userId.isBlank()) return null
        val body = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val userResponse = api.uploadUserAvatar(userId, part)   // <- UserResponse
        return userResponse.avatarUrl
    }

    suspend fun uploadTutorAvatar(tutorId: String, file: File): String {
        val body = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val map = api.uploadTutorAvatar(tutorId, part)
        return map["avatarUrl"].orEmpty()
    }
}
