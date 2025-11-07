// ApiService.kt
package com.example.Peerly.data

import com.example.Peerly.data.model.CreateSessionRequest
import com.example.Peerly.data.model.LoginRequest
import com.example.Peerly.data.model.SessionDto
import com.example.Peerly.data.model.UserCreateRequest
import com.example.Peerly.data.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Retrofit endpoints da API Peerly.
 * Base URL definida em RetrofitInstance (ex.: http://10.0.2.2:8080/).
 */
interface ApiService {

    /* -------------------- AUTH -------------------- */

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): UserResponse


    /* -------------------- USERS ------------------- */

    @GET("api/users")
    suspend fun getUsers(): List<UserResponse>

    @POST("api/users")
    suspend fun createUser(@Body body: UserCreateRequest): UserResponse

    @Multipart
    @POST("api/users/{id}/avatar")
    suspend fun uploadUserAvatar(
        @Path("id") userId: String,
        @Part file: MultipartBody.Part
    ): UserResponse


    /* -------------------- TUTORS ------------------ */
    // Mantém apenas se estiveres a usar avatars próprios de tutor.

    @Multipart
    @POST("api/tutors/{id}/avatar")
    suspend fun uploadTutorAvatar(
        @Path("id") tutorId: String,
        @Part file: MultipartBody.Part
    ): Map<String, String>


    /* ------------------- SESSIONS ----------------- */

    @POST("api/sessions")
    suspend fun createSession(@Body body: CreateSessionRequest): SessionDto

    @GET("api/sessions")
    suspend fun getSessions(): List<SessionDto>

    @GET("api/sessions/{id}")
    suspend fun getSessionById(@Path("id") id: String): SessionDto
}
