package com.example.Peerly.data

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class TutorRepository(
    private val api: ApiService = RetrofitInstance.api
) {


    suspend fun uploadAvatar(tutorId: String, file: File): String? = runCatching {
        val body = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("file", file.name, body)
        val res = api.uploadTutorAvatar(tutorId, part)
        res["avatarUrl"]
    }.getOrElse { null }


    suspend fun uploadAvatarFromUri(context: Context, tutorId: String, uri: Uri): String? {
        val tmp = copyUriToTemp(context, uri, "tutor_${tutorId}")
        return try {
            uploadAvatar(tutorId, tmp)
        } finally {

        }
    }

    private fun copyUriToTemp(context: Context, uri: Uri, baseName: String): File {
        val dir = File(context.cacheDir, "uploads").apply { if (!exists()) mkdirs() }
        val dest = File(dir, "${baseName}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(dest).use { out -> input.copyTo(out) }
        }
        return dest
    }
}
