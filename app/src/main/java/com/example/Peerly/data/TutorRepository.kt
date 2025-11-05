package com.example.Peerly.data

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TutorRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    suspend fun uploadAvatar(tutorId: String, file: File): String {
        val body = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val res = api.uploadTutorAvatar(tutorId, part)
        return res["avatarUrl"] ?: ""
    }
}
